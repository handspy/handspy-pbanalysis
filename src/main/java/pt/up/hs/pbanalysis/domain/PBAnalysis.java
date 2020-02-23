package pt.up.hs.pbanalysis.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * Analysis of pauses and busts in handwritten data.\n\n@author José Carlos Paiva
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
     * Sample to which the pause-burst analysis belongs
     */
    @Column(name = "sample")
    private Long sample;

    /**
     * Threshold used to identify bursts
     */
    @Column(name = "threshold")
    private Long threshold;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSample() {
        return sample;
    }

    public PBAnalysis sample(Long sample) {
        this.sample = sample;
        return this;
    }

    public void setSample(Long sample) {
        this.sample = sample;
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
            ", sample=" + getSample() +
            ", threshold=" + getThreshold() +
            "}";
    }
}
