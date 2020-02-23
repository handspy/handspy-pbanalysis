package pt.up.hs.pbanalysis.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PBAnalysisMapperTest {

    private PBAnalysisMapper pBAnalysisMapper;

    @BeforeEach
    public void setUp() {
        pBAnalysisMapper = new PBAnalysisMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(pBAnalysisMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(pBAnalysisMapper.fromId(null)).isNull();
    }
}
