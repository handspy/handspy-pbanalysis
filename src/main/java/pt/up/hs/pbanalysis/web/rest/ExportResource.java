package pt.up.hs.pbanalysis.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.up.hs.pbanalysis.service.ExportService;
import pt.up.hs.pbanalysis.service.dto.LengthUnit;
import pt.up.hs.pbanalysis.service.dto.TimeUnit;

import java.io.ByteArrayInputStream;

/**
 * REST controller for exporting pause-burst reports.
 */
@RestController
@RequestMapping("/api/projects/{projectId}")
public class ExportResource {

    private final Logger log = LoggerFactory.getLogger(ExportResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExportService exportService;

    public ExportResource(ExportService exportService) {
        this.exportService = exportService;
    }

    /**
     * {@code GET  /export} : export analysis with ID.
     *
     * @param projectId     ID of the project.
     * @param protocolId    ID of the protocol.
     * @param analysisId    ID of the analysis.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the excel file.
     */
    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> export(
        @PathVariable("projectId") Long projectId,
        @RequestParam(value = "protocolId") Long protocolId,
        @RequestParam(value = "analysisId") Long analysisId,
        @RequestParam(value = "burstId[]", required = false) Long[] burstIds,
        @RequestParam(value = "timeUnit", required = false, defaultValue = "MS") String timeUnitStr,
        @RequestParam(value = "lengthUnit", required = false, defaultValue = "MM") String lengthUnitStr
    ) {
        log.debug("REST request to export the analysis {} of protocol {} of project {}", analysisId, protocolId, projectId);

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

        ByteArrayInputStream bais = exportService.export(projectId, protocolId, analysisId, burstIds, timeUnit, lengthUnit);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/vnd.ms-excel");
        headers.add("Content-Disposition",
            "attachment; filename=pb_analysis_" + projectId + "_" + analysisId + ".xls");
        return ResponseEntity.ok()
            .headers(headers)
            .body(new InputStreamResource(bais));
    }

    /**
     * {@code GET  /export} : export analyses of text IDs.
     *
     * @param projectId     ID of the project.
     * @param protocolIds   IDs of the protocols analyzed.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the excel file.
     */
    @GetMapping("/bulk-export")
    public ResponseEntity<InputStreamResource> bulkExport(
        @PathVariable("projectId") Long projectId,
        @RequestParam(value = "protocolId[]") Long[] protocolIds,
        @RequestParam(value = "timeUnit", required = false, defaultValue = "MS") String timeUnitStr,
        @RequestParam(value = "lengthUnit", required = false, defaultValue = "MM") String lengthUnitStr
    ) {
        log.debug("REST request to bulk export the analyses of protocols {} of project {}", protocolIds, projectId);

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

        ByteArrayInputStream bais = exportService.bulkExport(projectId, protocolIds, timeUnit, lengthUnit);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/vnd.ms-excel");
        headers.add("Content-Disposition",
            "attachment; filename=pb_analyses_" + projectId + ".xls");
        return ResponseEntity.ok()
            .headers(headers)
            .body(new InputStreamResource(bais));
    }
}
