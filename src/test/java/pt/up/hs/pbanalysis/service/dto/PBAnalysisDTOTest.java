package pt.up.hs.pbanalysis.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pt.up.hs.pbanalysis.web.rest.TestUtil;

public class PBAnalysisDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PBAnalysisDTO.class);
        PBAnalysisDTO pBAnalysisDTO1 = new PBAnalysisDTO();
        pBAnalysisDTO1.setId(1L);
        PBAnalysisDTO pBAnalysisDTO2 = new PBAnalysisDTO();
        assertThat(pBAnalysisDTO1).isNotEqualTo(pBAnalysisDTO2);
        pBAnalysisDTO2.setId(pBAnalysisDTO1.getId());
        assertThat(pBAnalysisDTO1).isEqualTo(pBAnalysisDTO2);
        pBAnalysisDTO2.setId(2L);
        assertThat(pBAnalysisDTO1).isNotEqualTo(pBAnalysisDTO2);
        pBAnalysisDTO1.setId(null);
        assertThat(pBAnalysisDTO1).isNotEqualTo(pBAnalysisDTO2);
    }
}
