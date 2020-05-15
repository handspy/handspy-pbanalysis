package pt.up.hs.pbanalysis.client.sampling;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.up.hs.pbanalysis.client.AuthorizedUserFeignClient;
import pt.up.hs.pbanalysis.client.sampling.dto.Protocol;

import javax.validation.constraints.NotNull;

@AuthorizedUserFeignClient(name = "sampling")
public interface SamplingFeignClient {

    @RequestMapping(value = "/api/projects/{projectId}/protocols/{protocolId}/data", method = RequestMethod.GET)
    Protocol getProtocolData(
        @PathVariable("projectId") @NotNull Long projectId,
        @PathVariable("protocolId") @NotNull Long protocolId
    );
}
