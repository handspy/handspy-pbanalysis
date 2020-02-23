package pt.up.hs.pbanalysis.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * Metadata of the pause-burst analysis.\n\n@author José Carlos Paiva
 */
@Entity
@Table(name = "pb_metadata")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PBMetadata implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * Key of the metadata entry
     */
    @Column(name = "key")
    private String key;

    /**
     * Value of the metadata entry
     */
    @Column(name = "value")
    private String value;

    /**
     * A metadata entry is associated with a Pause-Burst analysis.
     */
    @ManyToOne
    @JsonIgnoreProperties("pBMetadata")
    private PBAnalysis analysis;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public PBMetadata key(String key) {
        this.key = key;
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public PBMetadata value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public PBAnalysis getAnalysis() {
        return analysis;
    }

    public PBMetadata analysis(PBAnalysis pBAnalysis) {
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
        if (!(o instanceof PBMetadata)) {
            return false;
        }
        return id != null && id.equals(((PBMetadata) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PBMetadata{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", value='" + getValue() + "'" +
            "}";
    }
}
