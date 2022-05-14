package pt.up.hs.pbanalysis.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pt.up.hs.pbanalysis.constants.EntityNames;
import pt.up.hs.pbanalysis.constants.ErrorKeys;
import pt.up.hs.pbanalysis.service.PBAnalysisQueryService;
import pt.up.hs.pbanalysis.service.PBAnalysisService;
import pt.up.hs.pbanalysis.service.dto.LengthUnit;
import pt.up.hs.pbanalysis.service.dto.PBAnalysisCriteria;
import pt.up.hs.pbanalysis.service.dto.PBAnalysisDTO;
import pt.up.hs.pbanalysis.service.dto.TimeUnit;
import pt.up.hs.pbanalysis.web.rest.errors.BadRequestAlertException;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link pt.up.hs.pbanalysis.domain.PBAnalysis}.
 */
@RestController
@RequestMapping("/api/projects/{projectId}/protocols/{protocolId}")
public class PBAnalysisResource {

    private final Logger log = LoggerFactory.getLogger(PBAnalysisResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PBAnalysisService pbAnalysisService;
    private final PBAnalysisQueryService pbAnalysisQueryService;

    public PBAnalysisResource(PBAnalysisService pbAnalysisService, PBAnalysisQueryService pbAnalysisQueryService) {
        this.pbAnalysisService = pbAnalysisService;
        this.pbAnalysisQueryService = pbAnalysisQueryService;
    }

    /**
     * {@code POST  /pb-analyses} : Create a new pause-burst analysis.
     *
     * @param projectId ID of the project of the analysis.
     * @param protocolId ID of the protocol analyzed.
     * @param pbAnalysisDTO the pbAnalysisDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pbAnalysisDTO, or with status {@code 400 (Bad Request)} if the pbAnalysis has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pb-analyses")
    public ResponseEntity<PBAnalysisDTO> createPBAnalysis(
        @PathVariable("projectId") Long projectId,
        @PathVariable("protocolId") Long protocolId,
        @RequestParam(name = "analyze", required = false, defaultValue = "true") boolean analyze,
        @Valid @RequestBody PBAnalysisDTO pbAnalysisDTO
    ) throws URISyntaxException {
        log.debug("REST request to save pause-burst analysis {} of project {} of protocol {}", pbAnalysisDTO, projectId, protocolId);
        if (pbAnalysisDTO.getId() != null) {
            throw new BadRequestAlertException("A new pause-burst analysis cannot already have an ID", EntityNames.PB_ANALYSIS, ErrorKeys.ERR_ID_EXISTS);
        }
        PBAnalysisDTO result;
        if (analyze) {
            result = pbAnalysisService.analyze(projectId, protocolId, pbAnalysisDTO);
        } else {
            result = pbAnalysisService.save(projectId, protocolId, pbAnalysisDTO);
        }
        return ResponseEntity.created(new URI("/api/projects/" + projectId + "/protocols/" + protocolId + "/pb-analyses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, EntityNames.PB_ANALYSIS, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pb-analyses} : Updates an existing pause-burst analysis.
     *
     * @param projectId ID of the project of the analysis.
     * @param protocolId ID of the protocol analyzed.
     * @param pbAnalysisDTO the pbAnalysisDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pbAnalysisDTO,
     * or with status {@code 400 (Bad Request)} if the pbAnalysisDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pbAnalysisDTO couldn't be updated.
     */
    @PutMapping("/pb-analyses")
    public ResponseEntity<PBAnalysisDTO> updatePBAnalysis(
        @PathVariable("projectId") Long projectId,
        @PathVariable("protocolId") Long protocolId,
        @RequestParam(name = "analyze", required = false, defaultValue = "false") boolean analyze,
        @Valid @RequestBody PBAnalysisDTO pbAnalysisDTO
    ) {
        log.debug("REST request to update pause-burst analysis {} of project {} of protocol {}", pbAnalysisDTO, projectId, protocolId);
        if (pbAnalysisDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", EntityNames.PB_ANALYSIS, ErrorKeys.ERR_ID_NULL);
        }
        PBAnalysisDTO result;
        if (analyze) {
            result = pbAnalysisService.analyze(projectId, protocolId, pbAnalysisDTO);
        } else {
            result = pbAnalysisService.save(projectId, protocolId, pbAnalysisDTO);
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, EntityNames.PB_ANALYSIS, pbAnalysisDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /pb-analyses} : get all the pause-burst analyses.
     *
     * @param projectId ID of the project of the analyses.
     * @param protocolId ID of the protocol of the analyses.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pBAnalyses in body.
     */
    @GetMapping("/pb-analyses")
    public ResponseEntity<List<PBAnalysisDTO>> getAllPBAnalyses(
        @PathVariable("projectId") Long projectId,
        @PathVariable("protocolId") Long protocolId,
        PBAnalysisCriteria criteria,
        @RequestParam(value = "timeUnit", required = false, defaultValue = "MS") String timeUnitStr,
        @RequestParam(value = "lengthUnit", required = false, defaultValue = "MM") String lengthUnitStr
    ) {
        log.debug("REST request to get PBAnalyses by criteria: {}", criteria);

        TimeUnit timeUnit;
        try {
            timeUnit = TimeUnit.valueOf(timeUnitStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            timeUnit = TimeUnit.MS;
        }

        LengthUnit lengthUnit;
        try {
            lengthUnit = LengthUnit.valueOf(lengthUnitStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            lengthUnit = LengthUnit.MM;
        }

        List<PBAnalysisDTO> pbAnalysisDTOs = pbAnalysisQueryService
            .findByCriteria(projectId, protocolId, timeUnit, lengthUnit, criteria);
        return ResponseEntity.ok().body(pbAnalysisDTOs);
    }

    /**
     * {@code GET  /pb-analyses/count} : count all the pause-burst analyses.
     *
     * @param projectId ID of the project of the analyses.
     * @param protocolId ID of the protocol of the analyses.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/pb-analyses/count")
    public ResponseEntity<Long> countPBAnalyses(
        @PathVariable("projectId") Long projectId,
        @PathVariable("protocolId") Long protocolId,
        PBAnalysisCriteria criteria
    ) {
        log.debug("REST request to count PBAnalyses by criteria: {}", criteria);
        return ResponseEntity.ok().body(pbAnalysisQueryService.countByCriteria(projectId, protocolId, criteria));
    }

    /**
     * {@code GET  /pb-analyses/:id} : get the "id" pause-burst analysis.
     *
     * @param projectId ID of the project of the analysis.
     * @param protocolId ID of the protocol of the analysis.
     * @param id the id of the pbAnalysisDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pbAnalysisDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pb-analyses/{id}")
    public ResponseEntity<PBAnalysisDTO> getPBAnalysis(
        @PathVariable("projectId") Long projectId,
        @PathVariable("protocolId") Long protocolId,
        @PathVariable Long id,
        @RequestParam(value = "timeUnit", required = false, defaultValue = "MS") String timeUnitStr,
        @RequestParam(value = "lengthUnit", required = false, defaultValue = "MM") String lengthUnitStr
    ) {
        log.debug("REST request to get pause-burst analysis {} of project {} of protocol {}", id, projectId, protocolId);

        TimeUnit timeUnit;
        try {
            timeUnit = TimeUnit.valueOf(timeUnitStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            timeUnit = TimeUnit.MS;
        }

        LengthUnit lengthUnit;
        try {
            lengthUnit = LengthUnit.valueOf(lengthUnitStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            lengthUnit = LengthUnit.MM;
        }

        Optional<PBAnalysisDTO> pbAnalysisDTO = pbAnalysisService.findOne(projectId, protocolId, id, timeUnit, lengthUnit);
        return ResponseUtil.wrapOrNotFound(pbAnalysisDTO);
    }

    /**
     * {@code DELETE  /pb-analyses/:id} : delete the "id" pause-burst analysis.
     *
     * @param projectId ID of the project of the analysis.
     * @param protocolId ID of the protocol of the analysis.
     * @param id the id of the pbAnalysisDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pb-analyses/{id}")
    public ResponseEntity<Void> deletePBAnalysis(
        @PathVariable("projectId") Long projectId,
        @PathVariable("protocolId") Long protocolId,
        @PathVariable Long id
    ) {
        log.debug("REST request to delete pause-burst analysis {} of project {} of protocol {}", id, projectId, protocolId);
        pbAnalysisService.delete(projectId, protocolId, id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, EntityNames.PB_ANALYSIS, id.toString()))
            .build();
    }
}
