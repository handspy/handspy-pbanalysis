package pt.up.hs.pbanalysis.client.project;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.up.hs.pbanalysis.client.AuthorizedUserFeignClient;
import pt.up.hs.pbanalysis.client.project.dto.Participant;

import javax.validation.constraints.NotNull;

@AuthorizedUserFeignClient(name = "project")
public interface ProjectFeignClient {

    @RequestMapping(value = "/api/projects/{projectId}/participants/{participantId}", method = RequestMethod.GET)
    Participant getParticipant(
        @PathVariable("projectId") @NotNull Long projectId,
        @PathVariable("participantId") @NotNull Long participantId
    );
}
