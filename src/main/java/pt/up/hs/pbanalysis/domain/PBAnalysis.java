package pt.up.hs.pbanalysis.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Analysis of pauses and bursts in handwritten data.
 *
 * @author José Carlos Paiva
 */
@Entity
@Table(name = "pb_analysis")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PBAnalysis extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * ID of the project on which this analysis has been conducted.
     */
    @NotNull
    @Column(name = "project_id", nullable = false)
    private Long projectId;

    /**
     * ID of the protocol on which this analysis has been conducted.
     */
    @NotNull
    @Column(name = "protocol_id", nullable = false)
    private Long protocolId;

    /**
     * Name of the analysis.
     */
    @NotNull
    @Size(max = 200)
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    /**
     * Description of the analysis.
     */
    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;

    /**
     * Threshold used to calculate bursts (in ms).
     */
    @NotNull
    @Column(name = "threshold", nullable = false)
    private Long threshold;

    @OneToMany(
        mappedBy = "analysis",
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY,
        orphanRemoval = true
    )
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnoreProperties("analysis")
    private Set<PBBurst> bursts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public PBAnalysis projectId(Long projectId) {
        this.projectId = projectId;
        return this;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
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

    public String getName() {
        return name;
    }

    public PBAnalysis name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public PBAnalysis description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
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
            ", projectId=" + getProjectId() +
            ", protocolId=" + getProtocolId() +
            ", name=" + getName() +
            ", description=" + getDescription() +
            ", threshold=" + getThreshold() +
            "}";
    }
}
