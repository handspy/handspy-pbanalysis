package pt.up.hs.pbanalysis.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pt.up.hs.pbanalysis.web.rest.TestUtil;

public class PBMetadataTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PBMetadata.class);
        PBMetadata pBMetadata1 = new PBMetadata();
        pBMetadata1.setId(1L);
        PBMetadata pBMetadata2 = new PBMetadata();
        pBMetadata2.setId(pBMetadata1.getId());
        assertThat(pBMetadata1).isEqualTo(pBMetadata2);
        pBMetadata2.setId(2L);
        assertThat(pBMetadata1).isNotEqualTo(pBMetadata2);
        pBMetadata1.setId(null);
        assertThat(pBMetadata1).isNotEqualTo(pBMetadata2);
    }
}
