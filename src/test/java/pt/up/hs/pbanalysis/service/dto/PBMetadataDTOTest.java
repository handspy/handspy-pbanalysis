package pt.up.hs.pbanalysis.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pt.up.hs.pbanalysis.web.rest.TestUtil;

public class PBMetadataDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PBMetadataDTO.class);
        PBMetadataDTO pBMetadataDTO1 = new PBMetadataDTO();
        pBMetadataDTO1.setId(1L);
        PBMetadataDTO pBMetadataDTO2 = new PBMetadataDTO();
        assertThat(pBMetadataDTO1).isNotEqualTo(pBMetadataDTO2);
        pBMetadataDTO2.setId(pBMetadataDTO1.getId());
        assertThat(pBMetadataDTO1).isEqualTo(pBMetadataDTO2);
        pBMetadataDTO2.setId(2L);
        assertThat(pBMetadataDTO1).isNotEqualTo(pBMetadataDTO2);
        pBMetadataDTO1.setId(null);
        assertThat(pBMetadataDTO1).isNotEqualTo(pBMetadataDTO2);
    }
}
