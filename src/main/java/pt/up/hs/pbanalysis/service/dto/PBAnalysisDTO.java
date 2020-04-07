package pt.up.hs.pbanalysis.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link pt.up.hs.pbanalysis.domain.PBAnalysis} entity.
 *
 * @author José Carlos Paiva
 */
@ApiModel(description = "Analysis of pauses and busts in handwritten data.")
public class PBAnalysisDTO implements Serializable {

    private Long id;

    /**
     * ID of the project on which this analysis has been conducted.
     */
    @NotNull
    @ApiModelProperty(value = "ID of the project on which this analysis has b" +
        "een conducted.")
    private Long projectId;

    /**
     * ID of the sample on which this analysis has been conducted.
     */
    @NotNull
    @ApiModelProperty(value = "ID of the sample on which this analysis has b" +
        "een conducted.")
    private Long sampleId;

    /**
     * ID of the protocol on which this analysis has been conducted.
     */
    @NotNull
    @ApiModelProperty(value = "ID of the protocol on which this analysis has" +
        " been conducted.")
    private Long protocolId;

    /**
     * Threshold used to calculate bursts (in ms).
     */
    @NotNull
    @ApiModelProperty(value = "Threshold used to calculate bursts (in ms).")
    private Long threshold;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getSampleId() {
        return sampleId;
    }

    public void setSampleId(Long sampleId) {
        this.sampleId = sampleId;
    }

    public Long getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(Long protocolId) {
        this.protocolId = protocolId;
    }

    public Long getThreshold() {
        return threshold;
    }

    public void setThreshold(Long threshold) {
        this.threshold = threshold;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PBAnalysisDTO pBAnalysisDTO = (PBAnalysisDTO) o;
        if (pBAnalysisDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pBAnalysisDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PBAnalysisDTO{" +
            "id=" + getId() +
            ", projectId=" + getProjectId() +
            ", sampleId=" + getSampleId() +
            ", protocolId=" + getProtocolId() +
            ", threshold=" + getThreshold() +
            "}";
    }
}
