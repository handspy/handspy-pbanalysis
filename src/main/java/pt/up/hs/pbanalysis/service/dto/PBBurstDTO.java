package pt.up.hs.pbanalysis.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.Duration;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link pt.up.hs.pbanalysis.domain.PBBurst} entity.
 */
@ApiModel(description = "Burst of the pause-burst analysis.\n\n@author José Carlos Paiva")
public class PBBurstDTO implements Serializable {

    private Long id;

    /**
     * Duration of the burst
     */
    @NotNull
    @ApiModelProperty(value = "Duration of the burst", required = true)
    private Duration duration;

    /**
     * Duration of the pause
     */
    @NotNull
    @ApiModelProperty(value = "Duration of the pause", required = true)
    private Duration pauseDuration;

    /**
     * Start X coordinate of burst
     */
    @NotNull
    @ApiModelProperty(value = "Start X coordinate of burst", required = true)
    private Integer startX;

    /**
     * Start Y coordinate of burst
     */
    @NotNull
    @ApiModelProperty(value = "Start Y coordinate of burst", required = true)
    private Integer startY;

    /**
     * End X coordinate of burst
     */
    @NotNull
    @ApiModelProperty(value = "End X coordinate of burst", required = true)
    private Integer endX;

    /**
     * End Y coordinate of burst
     */
    @NotNull
    @ApiModelProperty(value = "End Y coordinate of burst", required = true)
    private Integer endY;

    /**
     * Distance traveled during burst
     */
    @NotNull
    @ApiModelProperty(value = "Distance traveled during burst", required = true)
    private Double distance;

    /**
     * Average speed of burst
     */
    @NotNull
    @ApiModelProperty(value = "Average speed of burst", required = true)
    private Double avgSpeed;

    /**
     * Text slice written in burst
     */
    @ApiModelProperty(value = "Text slice written in burst")
    private String text;

    /**
     * A burst is part of a Pause-Burst analysis.
     */
    @ApiModelProperty(value = "A burst is part of a Pause-Burst analysis.")

    private Long analysisId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Duration getPauseDuration() {
        return pauseDuration;
    }

    public void setPauseDuration(Duration pauseDuration) {
        this.pauseDuration = pauseDuration;
    }

    public Integer getStartX() {
        return startX;
    }

    public void setStartX(Integer startX) {
        this.startX = startX;
    }

    public Integer getStartY() {
        return startY;
    }

    public void setStartY(Integer startY) {
        this.startY = startY;
    }

    public Integer getEndX() {
        return endX;
    }

    public void setEndX(Integer endX) {
        this.endX = endX;
    }

    public Integer getEndY() {
        return endY;
    }

    public void setEndY(Integer endY) {
        this.endY = endY;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(Double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(Long pBAnalysisId) {
        this.analysisId = pBAnalysisId;
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
            ", duration='" + getDuration() + "'" +
            ", pauseDuration='" + getPauseDuration() + "'" +
            ", startX=" + getStartX() +
            ", startY=" + getStartY() +
            ", endX=" + getEndX() +
            ", endY=" + getEndY() +
            ", distance=" + getDistance() +
            ", avgSpeed=" + getAvgSpeed() +
            ", text='" + getText() + "'" +
            ", analysisId=" + getAnalysisId() +
            "}";
    }
}
