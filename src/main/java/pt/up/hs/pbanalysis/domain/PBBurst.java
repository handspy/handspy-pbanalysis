package pt.up.hs.pbanalysis.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Duration;

/**
 * Burst of the pause-burst analysis.\n\n@author José Carlos Paiva
 */
@Entity
@Table(name = "pb_burst")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PBBurst implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * Duration of the burst
     */
    @NotNull
    @Column(name = "duration", nullable = false)
    private Duration duration;

    /**
     * Duration of the pause
     */
    @NotNull
    @Column(name = "pause_duration", nullable = false)
    private Duration pauseDuration;

    /**
     * Start X coordinate of burst
     */
    @NotNull
    @Column(name = "start_x", nullable = false)
    private Integer startX;

    /**
     * Start Y coordinate of burst
     */
    @NotNull
    @Column(name = "start_y", nullable = false)
    private Integer startY;

    /**
     * End X coordinate of burst
     */
    @NotNull
    @Column(name = "end_x", nullable = false)
    private Integer endX;

    /**
     * End Y coordinate of burst
     */
    @NotNull
    @Column(name = "end_y", nullable = false)
    private Integer endY;

    /**
     * Distance traveled during burst
     */
    @NotNull
    @Column(name = "distance", nullable = false)
    private Double distance;

    /**
     * Average speed of burst
     */
    @NotNull
    @Column(name = "avg_speed", nullable = false)
    private Double avgSpeed;

    /**
     * Text slice written in burst
     */
    @Column(name = "text")
    private String text;

    /**
     * A burst is part of a Pause-Burst analysis.
     */
    @ManyToOne
    @JsonIgnoreProperties("pBBursts")
    private PBAnalysis analysis;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Duration getDuration() {
        return duration;
    }

    public PBBurst duration(Duration duration) {
        this.duration = duration;
        return this;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Duration getPauseDuration() {
        return pauseDuration;
    }

    public PBBurst pauseDuration(Duration pauseDuration) {
        this.pauseDuration = pauseDuration;
        return this;
    }

    public void setPauseDuration(Duration pauseDuration) {
        this.pauseDuration = pauseDuration;
    }

    public Integer getStartX() {
        return startX;
    }

    public PBBurst startX(Integer startX) {
        this.startX = startX;
        return this;
    }

    public void setStartX(Integer startX) {
        this.startX = startX;
    }

    public Integer getStartY() {
        return startY;
    }

    public PBBurst startY(Integer startY) {
        this.startY = startY;
        return this;
    }

    public void setStartY(Integer startY) {
        this.startY = startY;
    }

    public Integer getEndX() {
        return endX;
    }

    public PBBurst endX(Integer endX) {
        this.endX = endX;
        return this;
    }

    public void setEndX(Integer endX) {
        this.endX = endX;
    }

    public Integer getEndY() {
        return endY;
    }

    public PBBurst endY(Integer endY) {
        this.endY = endY;
        return this;
    }

    public void setEndY(Integer endY) {
        this.endY = endY;
    }

    public Double getDistance() {
        return distance;
    }

    public PBBurst distance(Double distance) {
        this.distance = distance;
        return this;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getAvgSpeed() {
        return avgSpeed;
    }

    public PBBurst avgSpeed(Double avgSpeed) {
        this.avgSpeed = avgSpeed;
        return this;
    }

    public void setAvgSpeed(Double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public String getText() {
        return text;
    }

    public PBBurst text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public PBAnalysis getAnalysis() {
        return analysis;
    }

    public PBBurst analysis(PBAnalysis pBAnalysis) {
        this.analysis = pBAnalysis;
        return this;
    }

    public void setAnalysis(PBAnalysis pBAnalysis) {
        this.analysis = pBAnalysis;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PBBurst)) {
            return false;
        }
        return id != null && id.equals(((PBBurst) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PBBurst{" +
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
            "}";
    }
}
