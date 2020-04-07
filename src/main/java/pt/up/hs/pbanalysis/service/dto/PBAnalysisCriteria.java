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

/**
 * Criteria class for the {@link pt.up.hs.pbanalysis.domain.PBAnalysis} entity. This class is used
 * in {@link pt.up.hs.pbanalysis.web.rest.PBAnalysisResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /pb-analyses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PBAnalysisCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter threshold;

    private LongFilter burstsId;

    public PBAnalysisCriteria() {
    }

    public PBAnalysisCriteria(PBAnalysisCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.threshold = other.threshold == null ? null : other.threshold.copy();
        this.burstsId = other.burstsId == null ? null : other.burstsId.copy();
    }

    @Override
    public PBAnalysisCriteria copy() {
        return new PBAnalysisCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getThreshold() {
        return threshold;
    }

    public void setThreshold(LongFilter threshold) {
        this.threshold = threshold;
    }

    public LongFilter getBurstsId() {
        return burstsId;
    }

    public void setBurstsId(LongFilter burstsId) {
        this.burstsId = burstsId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PBAnalysisCriteria that = (PBAnalysisCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(threshold, that.threshold) &&
            Objects.equals(burstsId, that.burstsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        threshold,
        burstsId
        );
    }

    @Override
    public String toString() {
        return "PBAnalysisCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (threshold != null ? "threshold=" + threshold + ", " : "") +
                (burstsId != null ? "burstsId=" + burstsId + ", " : "") +
            "}";
    }

}
