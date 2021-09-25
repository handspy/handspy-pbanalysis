package pt.up.hs.pbanalysis.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link pt.up.hs.pbanalysis.domain.PBBurst} entity.
 *
 * @author José Carlos Paiva
 */
@ApiModel(description = "Burst of the pause-burst analysis.")
public class PBBurstDTO implements Serializable {

    private Long id;

    /**
     * A burst is part of a Pause-Burst analysis.
     */
    @ApiModelProperty(value = "A burst is part of a Pause-Burst analysis.")
    private Long analysisId;

    /**
     * Duration of the pause that precedes this burst (in ms).
     */
    @NotNull
    @Min(value = 0L)
    @ApiModelProperty(value = "Duration of the pause that precedes this burst (in ms).")
    private Long pauseDuration;

    /**
     * Start time of this burst.
     */
    @NotNull
    @Min(value = 0L)
    @ApiModelProperty(value = "Start time of this burst.")
    private Long startTime;

    /**
     * End time of this burst.
     */
    @NotNull
    @Min(value = 0L)
    @ApiModelProperty(value = "End time of this burst.")
    private Long endTime;

    /**
     * Start position of this burst in X-axis.
     */
    @NotNull
    @ApiModelProperty(value = "Start position of this burst in X-axis.")
    private Double startX;

    /**
     * Start position of this burst in Y-axis.
     */
    @NotNull
    @ApiModelProperty(value = "Start position of this burst in Y-axis.")
    private Double startY;

    /**
     * End position of this burst in X-axis.
     */
    @NotNull
    @ApiModelProperty(value = "End position of this burst in X-axis.")
    private Double endX;

    /**
     * End position of this burst in Y-axis.
     */
    @NotNull
    @ApiModelProperty(value = "End position of this burst in Y-axis.")
    private Double endY;

    /**
     * Number of captured dots in this burst.
     */
    @NotNull
    @Min(value = 1)
    @ApiModelProperty(value = "Number of captured dots in this burst.")
    private Integer dotCount;

    /**
     * Distance traveled in this burst.
     */
    @ApiModelProperty(value = "Distance traveled in this burst.")
    private Double distance;

    /**
     * Average pressure in this burst.
     */
    @ApiModelProperty(value = "Average pressure in this burst.")
    private Double pressure;

    /**
     * Text slice written in burst.
     */
    @Size(max = 500)
    @ApiModelProperty(value = "Text slice written in burst.")
    private String text;

    /**
     * Length (in words) of the text slice written in burst.
     */
    @ApiModelProperty(value = "Length (in words) of the text slice written in burst.")
    private Double length;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(Long pBAnalysisId) {
        this.analysisId = pBAnalysisId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Long getPauseDuration() {
        return pauseDuration;
    }

    public void setPauseDuration(Long pauseDuration) {
        this.pauseDuration = pauseDuration;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Double getStartX() {
        return startX;
    }

    public void setStartX(Double startX) {
        this.startX = startX;
    }

    public Double getStartY() {
        return startY;
    }

    public void setStartY(Double startY) {
        this.startY = startY;
    }

    public Double getEndX() {
        return endX;
    }

    public void setEndX(Double endX) {
        this.endX = endX;
    }

    public Double getEndY() {
        return endY;
    }

    public void setEndY(Double endY) {
        this.endY = endY;
    }

    public Integer getDotCount() {
        return dotCount;
    }

    public void setDotCount(Integer dotCount) {
        this.dotCount = dotCount;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PBBurstDTO pBBurstDTO = (PBBurstDTO) o;
        if (pBBurstDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pBBurstDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PBBurstDTO{" +
            "id=" + getId() +
            ", analysisId=" + getAnalysisId() +
            ", text='" + getText() + "'" +
            ", length=" + getLength() +
            ", pauseDuration=" + getPauseDuration() +
            ", startTime=" + getStartTime() +
            ", endTime=" + getEndTime() +
            ", startX=" + getStartX() +
            ", startY=" + getStartY() +
            ", endX=" + getEndX() +
            ", endY=" + getEndY() +
            ", distance=" + getDistance() +
            ", pressure=" + getPressure() +
            ", dotCount=" + getDotCount() +
            "}";
    }
}
