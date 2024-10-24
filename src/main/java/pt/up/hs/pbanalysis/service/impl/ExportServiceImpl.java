package pt.up.hs.pbanalysis.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.up.hs.pbanalysis.client.sampling.SamplingFeignClient;
import pt.up.hs.pbanalysis.client.sampling.dto.Protocol;
import pt.up.hs.pbanalysis.constants.EntityNames;
import pt.up.hs.pbanalysis.constants.ErrorKeys;
import pt.up.hs.pbanalysis.domain.PBAnalysis;
import pt.up.hs.pbanalysis.domain.PBBurst;
import pt.up.hs.pbanalysis.reporting.PBAnalysisReportBuilder;
import pt.up.hs.pbanalysis.repository.PBAnalysisRepository;
import pt.up.hs.pbanalysis.service.ExportService;
import pt.up.hs.pbanalysis.service.dto.LengthUnit;
import pt.up.hs.pbanalysis.service.dto.TimeUnit;
import pt.up.hs.pbanalysis.service.exceptions.ServiceException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing exports.
 */
@Service
@Transactional
public class ExportServiceImpl implements ExportService {

    private final Logger log = LoggerFactory.getLogger(ExportServiceImpl.class);

    private final MessageSource messageSource;

    private final PBAnalysisRepository pbAnalysisRepository;
    private final SamplingFeignClient samplingFeignClient;

    public ExportServiceImpl(
        MessageSource messageSource,
        PBAnalysisRepository analysisRepository,
        SamplingFeignClient samplingFeignClient
    ) {
        this.messageSource = messageSource;
        this.pbAnalysisRepository = analysisRepository;
        this.samplingFeignClient = samplingFeignClient;
    }

    @Override
    public ByteArrayInputStream export(
        Long projectId, Long protocolId, Long analysisId, Long[] burstIds,
        TimeUnit timeUnit, LengthUnit lengthUnit
    ) {

        PBAnalysisReportBuilder pbarBuilder = new PBAnalysisReportBuilder(messageSource);

        pbAnalysisRepository
            .findByProjectIdAndProtocolIdAndId(projectId, protocolId, analysisId)
            .ifPresent(pbAnalysis -> addReportToAnalysis(pbarBuilder, pbAnalysis, burstIds, timeUnit, lengthUnit));

        try {
            return pbarBuilder.conclude().saveToStream();
        } catch (IOException e) {
            throw new ServiceException(
                EntityNames.PB_ANALYSIS,
                ErrorKeys.ERR_EXPORT_EXCEL,
                "Could not export analysis to Excel."
            );
        }
    }

    @Override
    public ByteArrayInputStream bulkExport(
        Long projectId, Long[] protocolIds,
        TimeUnit timeUnit, LengthUnit lengthUnit
    ) {

        PBAnalysisReportBuilder pbarBuilder = new PBAnalysisReportBuilder(messageSource);

        Arrays.stream(protocolIds)
            .forEach(protocolId -> {
                PBAnalysis pbAnalysis = pbAnalysisRepository
                    .findFirstByProjectIdAndProtocolIdOrderByLastModifiedDateDesc(projectId, protocolId)
                    .orElse(null);
                if (pbAnalysis == null) {
                    return;
                }
                addReportToAnalysis(pbarBuilder, pbAnalysis, null, timeUnit, lengthUnit);
            });

        try {
            return pbarBuilder.conclude().saveToStream();
        } catch (IOException e) {
            throw new ServiceException(
                EntityNames.PB_ANALYSIS,
                ErrorKeys.ERR_EXPORT_EXCEL,
                "Could not export analyses to Excel."
            );
        }
    }

    private void addReportToAnalysis(
        PBAnalysisReportBuilder pbarBuilder, PBAnalysis pbAnalysis, Long[] burstIds,
        TimeUnit timeUnit, LengthUnit lengthUnit
    ) {

        if (pbAnalysis == null) {
            return;
        }

        Protocol protocol = samplingFeignClient.getProtocol(pbAnalysis.getProjectId(), pbAnalysis.getProtocolId());

        String code = String.format("T%d_P%d_%d", protocol.getTaskId(), protocol.getParticipantId(), protocol.getPageNumber());

        List<PBBurst> pbBursts = pbAnalysis.getBursts().parallelStream()
            .sorted(Comparator.comparingLong(PBBurst::getStartTime))
            .collect(Collectors.toList());

        List<Long> burstIdsList = null;
        if (burstIds != null && burstIds.length > 0) {
            burstIdsList = Arrays.asList(burstIds);
        }

        pbarBuilder.createPBAnalysisSheet(code, timeUnit, lengthUnit);

        double totalDuration = 0D;
        double totalPauseDuration = 0D;
        double totalDistance = 0D;
        double totalTotalTime = 0D;
        double totalLength = 0D;
        for (int i = 0; i < pbBursts.size(); i++) {
            PBBurst pbBurst = pbBursts.get(i);
            if (burstIdsList != null && !burstIdsList.contains(pbBurst.getId())) {
                continue;
            }
            double burstDuration = (pbBurst.getEndTime() - pbBurst.getStartTime()) * timeUnit.getRate();
            double pauseDuration = pbBurst.getPauseDuration() * timeUnit.getRate();
            double distance = pbBurst.getDistance() * lengthUnit.getRate();
            double totalTime = ((pbBurst.getEndTime() - pbBurst.getStartTime()) + pbBurst.getPauseDuration()) * timeUnit.getRate();
            pbarBuilder.newBurstLine(
                i + 1,
                burstDuration,
                pauseDuration,
                distance,
                totalTime,
                distance / burstDuration,
                pbBurst.getLength() == null ? 0 : pbBurst.getLength(),
                pbBurst.getLength() == null ? 0 : pbBurst.getLength() / burstDuration,
                pbBurst.getText() == null ? "" : pbBurst.getText()
            );

            totalDuration += burstDuration;
            totalPauseDuration += pauseDuration;
            totalDistance += distance;
            totalTotalTime += totalTime;
            totalLength += pbBurst.getLength() == null ? 0 : pbBurst.getLength();
        }
        pbarBuilder.newBurstLine(
            null,
            totalDuration,
            totalPauseDuration,
            totalDistance,
            totalTotalTime,
            totalDistance / totalDuration,
            totalLength,
            totalLength / totalDuration,
            ""
        );
        pbarBuilder.concludeSheet();
    }
}
