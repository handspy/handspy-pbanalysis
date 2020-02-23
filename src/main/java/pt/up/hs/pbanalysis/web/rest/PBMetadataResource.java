package pt.up.hs.pbanalysis.web.rest;

import pt.up.hs.pbanalysis.service.PBMetadataService;
import pt.up.hs.pbanalysis.web.rest.errors.BadRequestAlertException;
import pt.up.hs.pbanalysis.service.dto.PBMetadataDTO;
import pt.up.hs.pbanalysis.service.dto.PBMetadataCriteria;
import pt.up.hs.pbanalysis.service.PBMetadataQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link pt.up.hs.pbanalysis.domain.PBMetadata}.
 */
@RestController
@RequestMapping("/api")
public class PBMetadataResource {

    private final Logger log = LoggerFactory.getLogger(PBMetadataResource.class);

    private static final String ENTITY_NAME = "pbanalysisPbMetadata";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PBMetadataService pBMetadataService;

    private final PBMetadataQueryService pBMetadataQueryService;

    public PBMetadataResource(PBMetadataService pBMetadataService, PBMetadataQueryService pBMetadataQueryService) {
        this.pBMetadataService = pBMetadataService;
        this.pBMetadataQueryService = pBMetadataQueryService;
    }

    /**
     * {@code POST  /pb-metadata} : Create a new pBMetadata.
     *
     * @param pBMetadataDTO the pBMetadataDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pBMetadataDTO, or with status {@code 400 (Bad Request)} if the pBMetadata has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pb-metadata")
    public ResponseEntity<PBMetadataDTO> createPBMetadata(@RequestBody PBMetadataDTO pBMetadataDTO) throws URISyntaxException {
        log.debug("REST request to save PBMetadata : {}", pBMetadataDTO);
        if (pBMetadataDTO.getId() != null) {
            throw new BadRequestAlertException("A new pBMetadata cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PBMetadataDTO result = pBMetadataService.save(pBMetadataDTO);
        return ResponseEntity.created(new URI("/api/pb-metadata/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pb-metadata} : Updates an existing pBMetadata.
     *
     * @param pBMetadataDTO the pBMetadataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pBMetadataDTO,
     * or with status {@code 400 (Bad Request)} if the pBMetadataDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pBMetadataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pb-metadata")
    public ResponseEntity<PBMetadataDTO> updatePBMetadata(@RequestBody PBMetadataDTO pBMetadataDTO) throws URISyntaxException {
        log.debug("REST request to update PBMetadata : {}", pBMetadataDTO);
        if (pBMetadataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PBMetadataDTO result = pBMetadataService.save(pBMetadataDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pBMetadataDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /pb-metadata} : get all the pBMetadata.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pBMetadata in body.
     */
    @GetMapping("/pb-metadata")
    public ResponseEntity<List<PBMetadataDTO>> getAllPBMetadata(PBMetadataCriteria criteria) {
        log.debug("REST request to get PBMetadata by criteria: {}", criteria);
        List<PBMetadataDTO> entityList = pBMetadataQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /pb-metadata/count} : count all the pBMetadata.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/pb-metadata/count")
    public ResponseEntity<Long> countPBMetadata(PBMetadataCriteria criteria) {
        log.debug("REST request to count PBMetadata by criteria: {}", criteria);
        return ResponseEntity.ok().body(pBMetadataQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /pb-metadata/:id} : get the "id" pBMetadata.
     *
     * @param id the id of the pBMetadataDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pBMetadataDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pb-metadata/{id}")
    public ResponseEntity<PBMetadataDTO> getPBMetadata(@PathVariable Long id) {
        log.debug("REST request to get PBMetadata : {}", id);
        Optional<PBMetadataDTO> pBMetadataDTO = pBMetadataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pBMetadataDTO);
    }

    /**
     * {@code DELETE  /pb-metadata/:id} : delete the "id" pBMetadata.
     *
     * @param id the id of the pBMetadataDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pb-metadata/{id}")
    public ResponseEntity<Void> deletePBMetadata(@PathVariable Long id) {
        log.debug("REST request to delete PBMetadata : {}", id);
        pBMetadataService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
