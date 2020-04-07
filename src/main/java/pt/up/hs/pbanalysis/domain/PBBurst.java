package pt.up.hs.pbanalysis.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * Burst of the pause-burst analysis.
 *
 * @author José Carlos Paiva
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
     * Duration of the pause that precedes this burst (in ms).
     */
    @NotNull
    @Min(value = 0L)
    @Column(name = "pause_duration", nullable = false)
    private Long pauseDuration;

    /**
     * Start time of this burst.
     */
    @NotNull
    @Min(value = 0L)
    @Column(name = "start_time", nullable = false)
    private Long startTime;

    /**
     * End time of this burst.
     */
    @NotNull
    @Min(value = 0L)
    @Column(name = "end_time", nullable = false)
    private Long endTime;

    /**
     * Start position of this burst in X-axis.
     */
    @NotNull
    @Column(name = "start_x", nullable = false)
    private Double startX;

    /**
     * Start position of this burst in Y-axis.
     */
    @NotNull
    @Column(name = "start_y", nullable = false)
    private Double startY;

    /**
     * End position of this burst in X-axis.
     */
    @NotNull
    @Column(name = "end_x", nullable = false)
    private Double endX;

    /**
     * End position of this burst in Y-axis.
     */
    @NotNull
    @Column(name = "end_y", nullable = false)
    private Double endY;

    /**
     * Number of captured dots in this burst.
     */
    @NotNull
    @Min(value = 1)
    @Column(name = "dot_count", nullable = false)
    private Integer dotCount;

    /**
     * Distance traveled in this burst.
     */
    @Column(name = "distance")
    private Double distance;

    /**
     * Text slice written in burst.
     */
    @Column(name = "text")
    private String text;

    /**
     * A burst is part of a Pause-Burst analysis.
     */
    @ManyToOne
    @JsonIgnoreProperties("bursts")
    private PBAnalysis analysis;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getPauseDuration() {
        return pauseDuration;
    }

    public PBBurst pauseDuration(Long pauseDuration) {
        this.pauseDuration = pauseDuration;
        return this;
    }

    public void setPauseDuration(Long pauseDuration) {
        this.pauseDuration = pauseDuration;
    }

    public Long getStartTime() {
        return startTime;
    }

    public PBBurst startTime(Long startTime) {
        this.startTime = startTime;
        return this;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public PBBurst endTime(Long endTime) {
        this.endTime = endTime;
        return this;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Double getStartX() {
        return startX;
    }

    public PBBurst startX(Double startX) {
        this.startX = startX;
        return this;
    }

    public void setStartX(Double startX) {
        this.startX = startX;
    }

    public Double getStartY() {
        return startY;
    }

    public PBBurst startY(Double startY) {
        this.startY = startY;
        return this;
    }

    public void setStartY(Double startY) {
        this.startY = startY;
    }

    public Double getEndX() {
        return endX;
    }

    public PBBurst endX(Double endX) {
        this.endX = endX;
        return this;
    }

    public void setEndX(Double endX) {
        this.endX = endX;
    }

    public Double getEndY() {
        return endY;
    }

    public PBBurst endY(Double endY) {
        this.endY = endY;
        return this;
    }

    public void setEndY(Double endY) {
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

    public Integer getDotCount() {
        return dotCount;
    }

    public PBBurst dotCount(Integer dotCount) {
        this.dotCount = dotCount;
        return this;
    }

    public void setDotCount(Integer dotCount) {
        this.dotCount = dotCount;
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
            ", text='" + getText() + "'" +
            ", pauseDuration=" + getPauseDuration() +
            ", startTime=" + getStartTime() +
            ", endTime=" + getEndTime() +
            ", startX=" + getStartX() +
            ", startY=" + getStartY() +
            ", endX=" + getEndX() +
            ", endY=" + getEndY() +
            ", distance=" + getDistance() +
            ", dotCount=" + getDotCount() +
            "}";
    }
}
