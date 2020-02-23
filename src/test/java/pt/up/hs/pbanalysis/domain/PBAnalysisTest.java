package pt.up.hs.pbanalysis.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pt.up.hs.pbanalysis.web.rest.TestUtil;

public class PBAnalysisTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PBAnalysis.class);
        PBAnalysis pBAnalysis1 = new PBAnalysis();
        pBAnalysis1.setId(1L);
        PBAnalysis pBAnalysis2 = new PBAnalysis();
        pBAnalysis2.setId(pBAnalysis1.getId());
        assertThat(pBAnalysis1).isEqualTo(pBAnalysis2);
        pBAnalysis2.setId(2L);
        assertThat(pBAnalysis1).isNotEqualTo(pBAnalysis2);
        pBAnalysis1.setId(null);
        assertThat(pBAnalysis1).isNotEqualTo(pBAnalysis2);
    }
}
