package pt.up.hs.pbanalysis.web.rest;

import pt.up.hs.pbanalysis.service.PBBurstService;
import pt.up.hs.pbanalysis.web.rest.errors.BadRequestAlertException;
import pt.up.hs.pbanalysis.service.dto.PBBurstDTO;
import pt.up.hs.pbanalysis.service.dto.PBBurstCriteria;
import pt.up.hs.pbanalysis.service.PBBurstQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link pt.up.hs.pbanalysis.domain.PBBurst}.
 */
@RestController
@RequestMapping("/api")
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
     * {@code POST  /pb-bursts} : Create a new pBBurst.
     *
     * @param pBBurstDTO the pBBurstDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pBBurstDTO, or with status {@code 400 (Bad Request)} if the pBBurst has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pb-bursts")
    public ResponseEntity<PBBurstDTO> createPBBurst(@Valid @RequestBody PBBurstDTO pBBurstDTO) throws URISyntaxException {
        log.debug("REST request to save PBBurst : {}", pBBurstDTO);
        if (pBBurstDTO.getId() != null) {
            throw new BadRequestAlertException("A new pBBurst cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PBBurstDTO result = pBBurstService.save(pBBurstDTO);
        return ResponseEntity.created(new URI("/api/pb-bursts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pb-bursts} : Updates an existing pBBurst.
     *
     * @param pBBurstDTO the pBBurstDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pBBurstDTO,
     * or with status {@code 400 (Bad Request)} if the pBBurstDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pBBurstDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pb-bursts")
    public ResponseEntity<PBBurstDTO> updatePBBurst(@Valid @RequestBody PBBurstDTO pBBurstDTO) throws URISyntaxException {
        log.debug("REST request to update PBBurst : {}", pBBurstDTO);
        if (pBBurstDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PBBurstDTO result = pBBurstService.save(pBBurstDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pBBurstDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /pb-bursts} : get all the pBBursts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pBBursts in body.
     */
    @GetMapping("/pb-bursts")
    public ResponseEntity<List<PBBurstDTO>> getAllPBBursts(PBBurstCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PBBursts by criteria: {}", criteria);
        Page<PBBurstDTO> page = pBBurstQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pb-bursts/count} : count all the pBBursts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/pb-bursts/count")
    public ResponseEntity<Long> countPBBursts(PBBurstCriteria criteria) {
        log.debug("REST request to count PBBursts by criteria: {}", criteria);
        return ResponseEntity.ok().body(pBBurstQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /pb-bursts/:id} : get the "id" pBBurst.
     *
     * @param id the id of the pBBurstDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pBBurstDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pb-bursts/{id}")
    public ResponseEntity<PBBurstDTO> getPBBurst(@PathVariable Long id) {
        log.debug("REST request to get PBBurst : {}", id);
        Optional<PBBurstDTO> pBBurstDTO = pBBurstService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pBBurstDTO);
    }

    /**
     * {@code DELETE  /pb-bursts/:id} : delete the "id" pBBurst.
     *
     * @param id the id of the pBBurstDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pb-bursts/{id}")
    public ResponseEntity<Void> deletePBBurst(@PathVariable Long id) {
        log.debug("REST request to delete PBBurst : {}", id);
        pBBurstService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}