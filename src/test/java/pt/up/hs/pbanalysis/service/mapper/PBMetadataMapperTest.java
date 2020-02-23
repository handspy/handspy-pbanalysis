package pt.up.hs.pbanalysis.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PBMetadataMapperTest {

    private PBMetadataMapper pBMetadataMapper;

    @BeforeEach
    public void setUp() {
        pBMetadataMapper = new PBMetadataMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(pBMetadataMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(pBMetadataMapper.fromId(null)).isNull();
    }
}
