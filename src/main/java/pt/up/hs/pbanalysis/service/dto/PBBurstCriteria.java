package pt.up.hs.pbanalysis.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.DurationFilter;

/**
 * Criteria class for the {@link pt.up.hs.pbanalysis.domain.PBBurst} entity. This class is used
 * in {@link pt.up.hs.pbanalysis.web.rest.PBBurstResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /pb-bursts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PBBurstCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DurationFilter duration;

    private DurationFilter pauseDuration;

    private IntegerFilter startX;

    private IntegerFilter startY;

    private IntegerFilter endX;

    private IntegerFilter endY;

    private DoubleFilter distance;

    private DoubleFilter avgSpeed;

    private StringFilter text;

    private LongFilter analysisId;

    public PBBurstCriteria() {
    }

    public PBBurstCriteria(PBBurstCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.duration = other.duration == null ? null : other.duration.copy();
        this.pauseDuration = other.pauseDuration == null ? null : other.pauseDuration.copy();
        this.startX = other.startX == null ? null : other.startX.copy();
        this.startY = other.startY == null ? null : other.startY.copy();
        this.endX = other.endX == null ? null : other.endX.copy();
        this.endY = other.endY == null ? null : other.endY.copy();
        this.distance = other.distance == null ? null : other.distance.copy();
        this.avgSpeed = other.avgSpeed == null ? null : other.avgSpeed.copy();
        this.text = other.text == null ? null : other.text.copy();
        this.analysisId = other.analysisId == null ? null : other.analysisId.copy();
    }

    @Override
    public PBBurstCriteria copy() {
        return new PBBurstCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public DurationFilter getDuration() {
        return duration;
    }

    public void setDuration(DurationFilter duration) {
        this.duration = duration;
    }

    public DurationFilter getPauseDuration() {
        return pauseDuration;
    }

    public void setPauseDuration(DurationFilter pauseDuration) {
        this.pauseDuration = pauseDuration;
    }

    public IntegerFilter getStartX() {
        return startX;
    }

    public void setStartX(IntegerFilter startX) {
        this.startX = startX;
    }

    public IntegerFilter getStartY() {
        return startY;
    }

    public void setStartY(IntegerFilter startY) {
        this.startY = startY;
    }

    public IntegerFilter getEndX() {
        return endX;
    }

    public void setEndX(IntegerFilter endX) {
        this.endX = endX;
    }

    public IntegerFilter getEndY() {
        return endY;
    }

    public void setEndY(IntegerFilter endY) {
        this.endY = endY;
    }

    public DoubleFilter getDistance() {
        return distance;
    }

    public void setDistance(DoubleFilter distance) {
        this.distance = distance;
    }

    public DoubleFilter getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(DoubleFilter avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public StringFilter getText() {
        return text;
    }

    public void setText(StringFilter text) {
        this.text = text;
    }

    public LongFilter getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(LongFilter analysisId) {
        this.analysisId = analysisId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PBBurstCriteria that = (PBBurstCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(duration, that.duration) &&
            Objects.equals(pauseDuration, that.pauseDuration) &&
            Objects.equals(startX, that.startX) &&
            Objects.equals(startY, that.startY) &&
            Objects.equals(endX, that.endX) &&
            Objects.equals(endY, that.endY) &&
            Objects.equals(distance, that.distance) &&
            Objects.equals(avgSpeed, that.avgSpeed) &&
            Objects.equals(text, that.text) &&
            Objects.equals(analysisId, that.analysisId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        duration,
        pauseDuration,
        startX,
        startY,
        endX,
        endY,
        distance,
        avgSpeed,
        text,
        analysisId
        );
    }

    @Override
    public String toString() {
        return "PBBurstCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (duration != null ? "duration=" + duration + ", " : "") +
                (pauseDuration != null ? "pauseDuration=" + pauseDuration + ", " : "") +
                (startX != null ? "startX=" + startX + ", " : "") +
                (startY != null ? "startY=" + startY + ", " : "") +
                (endX != null ? "endX=" + endX + ", " : "") +
                (endY != null ? "endY=" + endY + ", " : "") +
                (distance != null ? "distance=" + distance + ", " : "") +
                (avgSpeed != null ? "avgSpeed=" + avgSpeed + ", " : "") +
                (text != null ? "text=" + text + ", " : "") +
                (analysisId != null ? "analysisId=" + analysisId + ", " : "") +
            "}";
    }

}
