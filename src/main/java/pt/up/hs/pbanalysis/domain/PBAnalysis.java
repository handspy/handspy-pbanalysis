package pt.up.hs.pbanalysis.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Analysis of pauses and busts in handwritten data.
 *
 * @author José Carlos Paiva
 */
@Entity
@Table(name = "pb_analysis")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PBAnalysis implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * ID of the sample on which this analysis has been conducted.
     */
    @NotNull
    @Column(name = "sample_id", nullable = false)
    private Long sampleId;

    /**
     * ID of the protocol on which this analysis has been conducted.
     */
    @NotNull
    @Column(name = "protocol_id", nullable = false)
    private Long protocolId;

    /**
     * Threshold used to calculate bursts (in ms).
     */
    @NotNull
    @Column(name = "threshold", nullable = false)
    private Long threshold;

    @OneToMany(mappedBy = "analysis")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PBBurst> bursts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getThreshold() {
        return threshold;
    }

    public PBAnalysis threshold(Long threshold) {
        this.threshold = threshold;
        return this;
    }

    public void setThreshold(Long threshold) {
        this.threshold = threshold;
    }

    public Long getProtocolId() {
        return protocolId;
    }

    public PBAnalysis protocolId(Long protocolId) {
        this.protocolId = protocolId;
        return this;
    }

    public void setProtocolId(Long protocolId) {
        this.protocolId = protocolId;
    }

    public Long getSampleId() {
        return sampleId;
    }

    public PBAnalysis sampleId(Long sampleId) {
        this.sampleId = sampleId;
        return this;
    }

    public void setSampleId(Long sampleId) {
        this.sampleId = sampleId;
    }

    public Set<PBBurst> getBursts() {
        return bursts;
    }

    public PBAnalysis bursts(Set<PBBurst> pBBursts) {
        this.bursts = pBBursts;
        return this;
    }

    public PBAnalysis addBursts(PBBurst pBBurst) {
        this.bursts.add(pBBurst);
        pBBurst.setAnalysis(this);
        return this;
    }

    public PBAnalysis removeBursts(PBBurst pBBurst) {
        this.bursts.remove(pBBurst);
        pBBurst.setAnalysis(null);
        return this;
    }

    public void setBursts(Set<PBBurst> pBBursts) {
        this.bursts = pBBursts;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PBAnalysis)) {
            return false;
        }
        return id != null && id.equals(((PBAnalysis) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PBAnalysis{" +
            "id=" + getId() +
            ", protocolId=" + getProtocolId() +
            ", sampleId=" + getSampleId() +
            ", threshold=" + getThreshold() +
            "}";
    }
}
