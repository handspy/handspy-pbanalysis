package pt.up.hs.pbanalysis.client.sampling;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.up.hs.pbanalysis.client.AuthorizedUserFeignClient;
import pt.up.hs.pbanalysis.client.sampling.dto.Stroke;

import javax.validation.constraints.NotNull;
import java.util.List;

@AuthorizedUserFeignClient(name = "sampling")
public interface SamplingFeignClient {

    @RequestMapping(value = "/api/projects/{projectId}/protocols/{protocolId}/strokes", method = RequestMethod.GET)
    List<Stroke> getProtocolStrokes(
        @PathVariable("projectId") @NotNull Long projectId,
        @PathVariable("protocolId") @NotNull Long protocolId
    );
}
