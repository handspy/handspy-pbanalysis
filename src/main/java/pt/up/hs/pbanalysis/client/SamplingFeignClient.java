package pt.up.hs.pbanalysis.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.up.hs.pbanalysis.client.dto.Stroke;

import java.util.List;

@AuthorizedUserFeignClient(name = "sampling")
public interface SamplingFeignClient {

    @RequestMapping(value = "/api/projects/{projectId}/protocols/{protocolId}/strokes", method = RequestMethod.GET)
    List<Stroke> getProtocolStrokes(@PathVariable("projectId") Long projectId, @PathVariable("protocolId") Long protocolId);
}
