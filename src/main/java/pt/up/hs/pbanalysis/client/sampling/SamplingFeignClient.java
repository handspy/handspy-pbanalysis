package pt.up.hs.pbanalysis.client.sampling;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.up.hs.pbanalysis.client.AuthorizedUserFeignClient;
import pt.up.hs.pbanalysis.client.sampling.dto.Protocol;
import pt.up.hs.pbanalysis.client.sampling.dto.ProtocolData;

import javax.validation.constraints.NotNull;

@AuthorizedUserFeignClient(name = "sampling")
public interface SamplingFeignClient {

    @RequestMapping(value = "/api/projects/{projectId}/protocols/{protocolId}", method = RequestMethod.GET)
    Protocol getProtocol(
        @PathVariable("projectId") @NotNull Long projectId,
        @PathVariable("protocolId") @NotNull Long protocolId
    );

    @RequestMapping(value = "/api/projects/{projectId}/protocols/{protocolId}/data", method = RequestMethod.GET)
    ProtocolData getProtocolData(
        @PathVariable("projectId") @NotNull Long projectId,
        @PathVariable("protocolId") @NotNull Long protocolId
    );
}
