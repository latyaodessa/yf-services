package yf.submission.rest;

import yf.submission.dtos.AllParticipantsDTO;
import yf.submission.dtos.SubmissionDTO;
import yf.submission.services.SubmissionService;
import yf.user.dto.AuthResponseStatusesEnum;
import yf.user.services.UserService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response initSubmission(@PathParam("userId") final Long userId, @PathParam("token") final String token, final AllParticipantsDTO allParticipantsDTO) {

        final Boolean isValid = userService.validateToken(userId,
                token);
        if (!isValid) {
            Response.status(401)
                    .entity(AuthResponseStatusesEnum.VERIFICATION_NOT_VALID)
                    .build();
        }
        return Response.status(200)
                .entity(submissionService.initSubmission(allParticipantsDTO, userId))
                .build();

    }

    @POST
    @Path("update/{userId}/{token}")
    public Response updateSubmission(@PathParam("userId") final Long userId, @PathParam("token") final String token, final SubmissionDTO submissionDTO) {
        final Boolean isValid = userService.validateToken(userId,
                token);
        if (!isValid) {
            Response.status(401)
                    .entity(AuthResponseStatusesEnum.VERIFICATION_NOT_VALID)
                    .build();
        }
        return Response.status(200)
                .entity(submissionService.updateSubmission(submissionDTO))
                .build();

    }

    @POST
    @Path("submit/{userId}/{token}")
    public Response submit(@PathParam("userId") final Long userId, @PathParam("token") final String token, final SubmissionDTO submissionDTO) {
        final Boolean isValid = userService.validateToken(userId,
                token);
        if (!isValid) {
            Response.status(401)
                    .entity(AuthResponseStatusesEnum.VERIFICATION_NOT_VALID)
                    .build();
        }
        return Response.status(200)
                .entity(submissionService.submit(submissionDTO))
                .build();

    }

    @GET
    @Path("{uuid}/{userId}/{token}")
    public Response getSubmissionByUuid(@PathParam("uuid") final String uuid, @PathParam("userId") final Long userId, @PathParam("token") final String token) {
        final Boolean isValid = userService.validateToken(userId,
                token);
        if (!isValid) {
            Response.status(401)
                    .entity(AuthResponseStatusesEnum.VERIFICATION_NOT_VALID)
                    .build();
        }
        return Response.status(200)
                .entity(submissionService.geSubmissionByUUid(uuid, userId))
                .build();

    }

}
