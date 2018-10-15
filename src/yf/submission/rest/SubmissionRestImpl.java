package yf.submission.rest;

import yf.submission.dtos.SubmissionParticipantDTO;
import yf.submission.services.SubmissionService;
import yf.user.dto.AuthResponseStatusesEnum;
import yf.user.services.UserService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("submission")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class SubmissionRestImpl {


    @Inject
    private SubmissionService submissionService;
    @Inject
    private UserService userService;

    @POST
    @Path("init/{userId}/{token}")
    public Response iniSubmission(
            @PathParam("userId") Long userId,
            @PathParam("token") String token,
            final List<SubmissionParticipantDTO> submissionParticipants) {
        final Boolean isValid = userService.validateToken(userId,
                token);
        if (!isValid) {
            Response.status(401)
                    .entity(AuthResponseStatusesEnum.VERIFICATION_NOT_VALID)
                    .build();
        }
        return Response.status(200)
                .entity(submissionService.initSubmission(submissionParticipants, userId))
                .build();
    }


}
