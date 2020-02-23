package pt.up.hs.pbanalysis.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pt.up.hs.pbanalysis.web.rest.TestUtil;

public class PBBurstTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PBBurst.class);
        PBBurst pBBurst1 = new PBBurst();
        pBBurst1.setId(1L);
        PBBurst pBBurst2 = new PBBurst();
        pBBurst2.setId(pBBurst1.getId());
        assertThat(pBBurst1).isEqualTo(pBBurst2);
        pBBurst2.setId(2L);
        assertThat(pBBurst1).isNotEqualTo(pBBurst2);
        pBBurst1.setId(null);
        assertThat(pBBurst1).isNotEqualTo(pBBurst2);
    }
}
