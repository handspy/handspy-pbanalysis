package pt.up.hs.pbanalysis.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pt.up.hs.pbanalysis.web.rest.TestUtil;

public class PBBurstDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PBBurstDTO.class);
        PBBurstDTO pBBurstDTO1 = new PBBurstDTO();
        pBBurstDTO1.setId(1L);
        PBBurstDTO pBBurstDTO2 = new PBBurstDTO();
        assertThat(pBBurstDTO1).isNotEqualTo(pBBurstDTO2);
        pBBurstDTO2.setId(pBBurstDTO1.getId());
        assertThat(pBBurstDTO1).isEqualTo(pBBurstDTO2);
        pBBurstDTO2.setId(2L);
        assertThat(pBBurstDTO1).isNotEqualTo(pBBurstDTO2);
        pBBurstDTO1.setId(null);
        assertThat(pBBurstDTO1).isNotEqualTo(pBBurstDTO2);
    }
}
