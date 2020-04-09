package pt.up.hs.pbanalysis.service.dto;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.*;

import java.io.Serializable;
import java.util.Objects;

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

    private StringFilter text;

    private LongFilter pauseDuration;

    private LongFilter startTime;

    private LongFilter endTime;

    private DoubleFilter startX;

    private DoubleFilter startY;

    private DoubleFilter endX;

    private DoubleFilter endY;

    private DoubleFilter distance;

    private DoubleFilter pressure;

    private IntegerFilter dotCount;

    public PBBurstCriteria() {
    }

    public PBBurstCriteria(PBBurstCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.text = other.text == null ? null : other.text.copy();
        this.pauseDuration = other.pauseDuration == null ? null : other.pauseDuration.copy();
        this.startTime = other.startTime == null ? null : other.startTime.copy();
        this.endTime = other.endTime == null ? null : other.endTime.copy();
        this.startX = other.startX == null ? null : other.startX.copy();
        this.startY = other.startY == null ? null : other.startY.copy();
        this.endX = other.endX == null ? null : other.endX.copy();
        this.endY = other.endY == null ? null : other.endY.copy();
        this.distance = other.distance == null ? null : other.distance.copy();
        this.pressure = other.pressure == null ? null : other.pressure.copy();
        this.dotCount = other.dotCount == null ? null : other.dotCount.copy();
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

    public StringFilter getText() {
        return text;
    }

    public void setText(StringFilter text) {
        this.text = text;
    }

    public LongFilter getPauseDuration() {
        return pauseDuration;
    }

    public void setPauseDuration(LongFilter pauseDuration) {
        this.pauseDuration = pauseDuration;
    }

    public LongFilter getStartTime() {
        return startTime;
    }

    public void setStartTime(LongFilter startTime) {
        this.startTime = startTime;
    }

    public LongFilter getEndTime() {
        return endTime;
    }

    public void setEndTime(LongFilter endTime) {
        this.endTime = endTime;
    }

    public DoubleFilter getStartX() {
        return startX;
    }

    public void setStartX(DoubleFilter startX) {
        this.startX = startX;
    }

    public DoubleFilter getStartY() {
        return startY;
    }

    public void setStartY(DoubleFilter startY) {
        this.startY = startY;
    }

    public DoubleFilter getEndX() {
        return endX;
    }

    public void setEndX(DoubleFilter endX) {
        this.endX = endX;
    }

    public DoubleFilter getEndY() {
        return endY;
    }

    public void setEndY(DoubleFilter endY) {
        this.endY = endY;
    }

    public DoubleFilter getDistance() {
        return distance;
    }

    public void setDistance(DoubleFilter distance) {
        this.distance = distance;
    }

    public DoubleFilter getPressure() {
        return pressure;
    }

    public void setPressure(DoubleFilter pressure) {
        this.pressure = pressure;
    }

    public IntegerFilter getDotCount() {
        return dotCount;
    }

    public void setDotCount(IntegerFilter dotCount) {
        this.dotCount = dotCount;
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
                Objects.equals(text, that.text) &&
                Objects.equals(pauseDuration, that.pauseDuration) &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(endTime, that.endTime) &&
                Objects.equals(startX, that.startX) &&
                Objects.equals(startY, that.startY) &&
                Objects.equals(endX, that.endX) &&
                Objects.equals(endY, that.endY) &&
                Objects.equals(distance, that.distance) &&
                Objects.equals(pressure, that.pressure) &&
                Objects.equals(dotCount, that.dotCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            text,
            pauseDuration,
            startTime,
            endTime,
            startX,
            startY,
            endX,
            endY,
            distance,
            pressure,
            dotCount
        );
    }

    @Override
    public String toString() {
        return "PBBurstCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (text != null ? "text=" + text + ", " : "") +
            (pauseDuration != null ? "pauseDuration=" + pauseDuration + ", " : "") +
            (startTime != null ? "startTime=" + startTime + ", " : "") +
            (endTime != null ? "endTime=" + endTime + ", " : "") +
            (startX != null ? "startX=" + startX + ", " : "") +
            (startY != null ? "startY=" + startY + ", " : "") +
            (endX != null ? "endX=" + endX + ", " : "") +
            (endY != null ? "endY=" + endY + ", " : "") +
            (distance != null ? "distance=" + distance + ", " : "") +
            (pressure != null ? "pressure=" + pressure + ", " : "") +
            (dotCount != null ? "dotCount=" + dotCount + ", " : "") +
            "}";
    }
}
