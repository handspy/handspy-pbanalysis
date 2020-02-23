package pt.up.hs.pbanalysis.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link pt.up.hs.pbanalysis.domain.PBMetadata} entity.
 */
@ApiModel(description = "Metadata of the pause-burst analysis.\n\n@author José Carlos Paiva")
public class PBMetadataDTO implements Serializable {

    private Long id;

    /**
     * Key of the metadata entry
     */
    @ApiModelProperty(value = "Key of the metadata entry")
    private String key;

    /**
     * Value of the metadata entry
     */
    @ApiModelProperty(value = "Value of the metadata entry")
    private String value;

    /**
     * A metadata entry is associated with a Pause-Burst analysis.
     */
    @ApiModelProperty(value = "A metadata entry is associated with a Pause-Burst analysis.")

    private Long analysisId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

        PBMetadataDTO pBMetadataDTO = (PBMetadataDTO) o;
        if (pBMetadataDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pBMetadataDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PBMetadataDTO{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", value='" + getValue() + "'" +
            ", analysisId=" + getAnalysisId() +
            "}";
    }
}
