package pt.up.hs.pbanalysis.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PBBurstMapperTest {

    private PBBurstMapper pBBurstMapper;

    @BeforeEach
    public void setUp() {
        pBBurstMapper = new PBBurstMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(pBBurstMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(pBBurstMapper.fromId(null)).isNull();
    }
}
