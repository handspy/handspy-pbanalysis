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
import pt.up.hs.pbanalysis.service.PBAnalysisQueryService;
import pt.up.hs.pbanalysis.service.PBAnalysisService;
import pt.up.hs.pbanalysis.service.dto.PBAnalysisCriteria;
import pt.up.hs.pbanalysis.service.dto.PBAnalysisDTO;
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
@RequestMapping("/api/projects/{projectId}/samples/{sampleId}/protocols/{protocolId}")
public class PBAnalysisResource {

    private final Logger log = LoggerFactory.getLogger(PBAnalysisResource.class);

    private static final String ENTITY_NAME = "pbanalysisPbAnalysis";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PBAnalysisService pBAnalysisService;

    private final PBAnalysisQueryService pBAnalysisQueryService;

    public PBAnalysisResource(PBAnalysisService pBAnalysisService, PBAnalysisQueryService pBAnalysisQueryService) {
        this.pBAnalysisService = pBAnalysisService;
        this.pBAnalysisQueryService = pBAnalysisQueryService;
    }

    /**
     * {@code POST  /pb-analyses} : Create a new pBAnalysis.
     *
     * @param projectId ID of the project of the analysis.
     * @param sampleId ID of the sample of the analysis.
     * @param protocolId ID of the protocol analyzed.
     * @param pBAnalysisDTO the pBAnalysisDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pBAnalysisDTO, or with status {@code 400 (Bad Request)} if the pBAnalysis has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pb-analyses")
    public ResponseEntity<PBAnalysisDTO> createPBAnalysis(
        @PathVariable("projectId") Long projectId,
        @PathVariable("sampleId") Long sampleId,
        @PathVariable("protocolId") Long protocolId,
        @Valid @RequestBody PBAnalysisDTO pBAnalysisDTO
    ) throws URISyntaxException {
        log.debug("REST request to save PBAnalysis : {}", pBAnalysisDTO);
        if (pBAnalysisDTO.getId() != null) {
            throw new BadRequestAlertException("A new pBAnalysis cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PBAnalysisDTO result = pBAnalysisService.save(pBAnalysisDTO);
        return ResponseEntity.created(new URI("/api/pb-analyses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pb-analyses} : Updates an existing pBAnalysis.
     *
     * @param projectId ID of the project of the analysis.
     * @param sampleId ID of the sample of the analysis.
     * @param protocolId ID of the protocol analyzed.
     * @param pBAnalysisDTO the pBAnalysisDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pBAnalysisDTO,
     * or with status {@code 400 (Bad Request)} if the pBAnalysisDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pBAnalysisDTO couldn't be updated.
     */
    @PutMapping("/pb-analyses")
    public ResponseEntity<PBAnalysisDTO> updatePBAnalysis(
        @PathVariable("projectId") Long projectId,
        @PathVariable("sampleId") Long sampleId,
        @PathVariable("protocolId") Long protocolId,
        @Valid @RequestBody PBAnalysisDTO pBAnalysisDTO
    ) {
        log.debug("REST request to update PBAnalysis : {}", pBAnalysisDTO);
        if (pBAnalysisDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PBAnalysisDTO result = pBAnalysisService.save(pBAnalysisDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pBAnalysisDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /pb-analyses} : get all the pBAnalyses.
     *
     * @param projectId ID of the project of the analyses.
     * @param sampleId ID of the sample of the analyses.
     * @param protocolId ID of the protocol of the analyses.
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pBAnalyses in body.
     */
    @GetMapping("/pb-analyses")
    public ResponseEntity<List<PBAnalysisDTO>> getAllPBAnalyses(
        @PathVariable("projectId") Long projectId,
        @PathVariable("sampleId") Long sampleId,
        @PathVariable("protocolId") Long protocolId,
        PBAnalysisCriteria criteria, Pageable pageable
    ) {
        log.debug("REST request to get PBAnalyses by criteria: {}", criteria);
        Page<PBAnalysisDTO> page = pBAnalysisQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pb-analyses/count} : count all the pBAnalyses.
     *
     * @param projectId ID of the project of the analyses.
     * @param sampleId ID of the sample of the analyses.
     * @param protocolId ID of the protocol of the analyses.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/pb-analyses/count")
    public ResponseEntity<Long> countPBAnalyses(
        @PathVariable("projectId") Long projectId,
        @PathVariable("sampleId") Long sampleId,
        @PathVariable("protocolId") Long protocolId,
        PBAnalysisCriteria criteria
    ) {
        log.debug("REST request to count PBAnalyses by criteria: {}", criteria);
        return ResponseEntity.ok().body(pBAnalysisQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /pb-analyses/:id} : get the "id" pBAnalysis.
     *
     * @param projectId ID of the project of the analysis.
     * @param sampleId ID of the sample of the analysis.
     * @param protocolId ID of the protocol of the analysis.
     * @param id the id of the pBAnalysisDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pBAnalysisDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pb-analyses/{id}")
    public ResponseEntity<PBAnalysisDTO> getPBAnalysis(
        @PathVariable("projectId") Long projectId,
        @PathVariable("sampleId") Long sampleId,
        @PathVariable("protocolId") Long protocolId,
        @PathVariable Long id
    ) {
        log.debug("REST request to get PBAnalysis : {}", id);
        Optional<PBAnalysisDTO> pBAnalysisDTO = pBAnalysisService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pBAnalysisDTO);
    }

    /**
     * {@code DELETE  /pb-analyses/:id} : delete the "id" pBAnalysis.
     *
     * @param projectId ID of the project of the analysis.
     * @param sampleId ID of the sample of the analysis.
     * @param protocolId ID of the protocol of the analysis.
     * @param id the id of the pBAnalysisDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pb-analyses/{id}")
    public ResponseEntity<Void> deletePBAnalysis(
        @PathVariable("projectId") Long projectId,
        @PathVariable("sampleId") Long sampleId,
        @PathVariable("protocolId") Long protocolId,
        @PathVariable Long id
    ) {
        log.debug("REST request to delete PBAnalysis : {}", id);
        pBAnalysisService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
