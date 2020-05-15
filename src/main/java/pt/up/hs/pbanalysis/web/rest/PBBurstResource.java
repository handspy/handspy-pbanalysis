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
import pt.up.hs.pbanalysis.service.PBBurstQueryService;
import pt.up.hs.pbanalysis.service.PBBurstService;
import pt.up.hs.pbanalysis.service.dto.PBBurstCriteria;
import pt.up.hs.pbanalysis.service.dto.PBBurstDTO;
import pt.up.hs.pbanalysis.web.rest.errors.BadRequestAlertException;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link pt.up.hs.pbanalysis.domain.PBBurst}.
 */
@RestController
@RequestMapping("/api/projects/{projectId}/protocols/{protocolId}/pb-analyses/{analysisId}")
public class PBBurstResource {

    private final Logger log = LoggerFactory.getLogger(PBBurstResource.class);

    private static final String ENTITY_NAME = "pbanalysisPbBurst";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PBBurstService pBBurstService;

    private final PBBurstQueryService pBBurstQueryService;

    public PBBurstResource(PBBurstService pBBurstService, PBBurstQueryService pBBurstQueryService) {
        this.pBBurstService = pBBurstService;
        this.pBBurstQueryService = pBBurstQueryService;
    }

    /**
     * {@code POST  /pb-bursts} : Create a new pause-burst burst.
     *
     * @param projectId  ID of the project to which the burst belongs.
     * @param protocolId ID of the protocol to which the burst belongs.
     * @param analysisId ID of the analysis to which the burst belongs.
     * @param pbBurstDTO the pBBurstDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pBBurstDTO, or with status {@code 400 (Bad Request)} if the pBBurst has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pb-bursts")
    public ResponseEntity<PBBurstDTO> createPBBurst(
        @PathVariable("projectId") Long projectId,
        @PathVariable("protocolId") Long protocolId,
        @PathVariable("analysisId") Long analysisId,
        @Valid @RequestBody PBBurstDTO pbBurstDTO
    ) throws URISyntaxException {
        log.debug("REST request to save pause-burst burst {} of project {} of protocol {} of analysis {}", pbBurstDTO, projectId, protocolId, analysisId);
        if (pbBurstDTO.getId() != null) {
            throw new BadRequestAlertException("A new pause-burst burst cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PBBurstDTO result = pBBurstService.save(projectId, protocolId, analysisId, pbBurstDTO);
        return ResponseEntity.created(new URI("/api/projects/" + projectId + "/protocols/" + protocolId + "/pb-analyses/" + analysisId + "/pb-bursts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pb-bursts} : Updates an existing pause-burst burst.
     *
     * @param projectId  ID of the project to which the burst belongs.
     * @param protocolId ID of the protocol to which the burst belongs.
     * @param analysisId ID of the analysis to which the burst belongs.
     * @param pbBurstDTO the pBBurstDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pBBurstDTO,
     * or with status {@code 400 (Bad Request)} if the pBBurstDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pBBurstDTO couldn't be updated.
     */
    @PutMapping("/pb-bursts")
    public ResponseEntity<PBBurstDTO> updatePBBurst(
        @PathVariable("projectId") Long projectId,
        @PathVariable("protocolId") Long protocolId,
        @PathVariable("analysisId") Long analysisId,
        @Valid @RequestBody PBBurstDTO pbBurstDTO
    ) {
        log.debug("REST request to update pause-burst burst {} of project {} of protocol {} of analysis {}", pbBurstDTO, projectId, protocolId, analysisId);
        if (pbBurstDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PBBurstDTO result = pBBurstService.save(projectId, protocolId, analysisId, pbBurstDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pbBurstDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /pb-bursts} : get all the pause-burst bursts.
     *
     * @param projectId  ID of the project to which the bursts belong.
     * @param protocolId ID of the protocol to which the bursts belong.
     * @param analysisId ID of the analysis to which the bursts belong.
     * @param pageable   the pagination information.
     * @param criteria   the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the
     * list of pBBursts in body.
     */
    @GetMapping("/pb-bursts")
    public ResponseEntity<List<PBBurstDTO>> getAllPBBursts(
        @PathVariable("projectId") Long projectId,
        @PathVariable("protocolId") Long protocolId,
        @PathVariable("analysisId") Long analysisId,
        PBBurstCriteria criteria, Pageable pageable
    ) {
        log.debug("REST request to get PBBursts by criteria: {}", criteria);
        Page<PBBurstDTO> page = pBBurstQueryService
            .findByCriteria(projectId, protocolId, analysisId, criteria, pageable);
        HttpHeaders headers = PaginationUtil
            .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pb-bursts/count} : count all the pause-burst bursts.
     *
     * @param projectId  ID of the project to which the bursts belong.
     * @param protocolId ID of the protocol to which the bursts belong.
     * @param analysisId ID of the analysis to which the bursts belong.
     * @param criteria   the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/pb-bursts/count")
    public ResponseEntity<Long> countPBBursts(
        @PathVariable("projectId") Long projectId,
        @PathVariable("protocolId") Long protocolId,
        @PathVariable("analysisId") Long analysisId,
        PBBurstCriteria criteria
    ) {
        log.debug("REST request to count PBBursts by criteria: {}", criteria);
        return ResponseEntity.ok()
            .body(pBBurstQueryService.countByCriteria(projectId, protocolId, analysisId, criteria));
    }

    /**
     * {@code GET  /pb-bursts/:id} : get the "id" pause-burst burst.
     *
     * @param projectId  ID of the project to which the burst belongs.
     * @param protocolId ID of the protocol to which the burst belongs.
     * @param analysisId ID of the analysis to which the burst belongs.
     * @param id         the id of the entity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     * body the pBBurstDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pb-bursts/{id}")
    public ResponseEntity<PBBurstDTO> getPBBurst(
        @PathVariable("projectId") Long projectId,
        @PathVariable("protocolId") Long protocolId,
        @PathVariable("analysisId") Long analysisId,
        @PathVariable Long id
    ) {
        log.debug("REST request to get pause-burst burst {} of project {} of protocol {} of analysis {}", id, projectId, protocolId, analysisId);
        Optional<PBBurstDTO> pBBurstDTO = pBBurstService.findOne(projectId, protocolId, analysisId, id);
        return ResponseUtil.wrapOrNotFound(pBBurstDTO);
    }

    /**
     * {@code DELETE  /pb-bursts/:id} : delete the "id" pause-burst burst.
     *
     * @param projectId  ID of the project to which the burst belongs.
     * @param protocolId ID of the protocol to which the burst belongs.
     * @param analysisId ID of the analysis to which the burst belongs.
     * @param id         the id of the entity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pb-bursts/{id}")
    public ResponseEntity<Void> deletePBBurst(
        @PathVariable("projectId") Long projectId,
        @PathVariable("protocolId") Long protocolId,
        @PathVariable("analysisId") Long analysisId,
        @PathVariable Long id
    ) {
        log.debug("REST request to delete pause-burst burst {} of project {} of protocol {} of analysis {}", id, projectId, protocolId, analysisId);
        pBBurstService.delete(projectId, protocolId, analysisId, id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
