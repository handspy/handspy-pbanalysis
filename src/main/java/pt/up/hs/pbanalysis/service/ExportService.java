package pt.up.hs.pbanalysis.service;

import pt.up.hs.pbanalysis.service.dto.LengthUnit;
import pt.up.hs.pbanalysis.service.dto.TimeUnit;

import java.io.ByteArrayInputStream;

/**
 * Service Interface for managing exports.
 *
 * @author José Carlos Paiva
 */
public interface ExportService {

    /**
     * Export an analysis of a protocol.
     *
     * @param projectId {@link Long} ID of the project.
     * @param protocolId {@link Long} ID of the protocol from which the analysis is to export.
     * @param analysisId {@link Long} ID of the analysis to export.
     * @param burstIds {@link Long[] IDs of the bursts to export.
     * @return {@link ByteArrayInputStream} of the created workbook.
     */
    ByteArrayInputStream export(Long projectId, Long protocolId, Long analysisId, Long[] burstIds, TimeUnit timeUnit, LengthUnit lengthUnit);

    /**
     * Bulk export the analyses of protocols.
     *
     * @param projectId {@link Long} ID of the project.
     * @param protocolIds {@link Long[]} ID of the protocols from which the analyses are to export.
     * @return {@link ByteArrayInputStream} of the created workbook.
     */
    ByteArrayInputStream bulkExport(Long projectId, Long[] protocolIds, TimeUnit timeUnit, LengthUnit lengthUnit);
}
