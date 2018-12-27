package yf.submission.rest;

import yf.submission.dtos.AllParticipantsDTO;
import yf.submission.dtos.SubmissionDTO;
import yf.submission.dtos.SubmissionStatusEnum;
import yf.submission.services.SubmissionService;
import yf.user.dto.AuthResponseStatusesEnum;
import yf.user.services.UserService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
    @Path("all/{userId}/{token}/{offset}/{limit}")
    public Response getAllUserSubmissions(@PathParam("userId") final Long userId, @PathParam("token") final String token,
                                          @PathParam("offset") final Integer offset, @PathParam("limit") final Integer limit) {
        final Boolean isValid = userService.validateToken(userId,
                token);
        if (!isValid) {
            Response.status(401)
                    .entity(AuthResponseStatusesEnum.VERIFICATION_NOT_VALID)
                    .build();
        }
        return Response.status(200)
                .entity(submissionService.getUserSubmissions(userId, offset, limit))
                .build();

    }

    @GET
    @Path("status/{statusEnum}/{userId}/{token}")
    public Response getSubmissionsByStatus(@PathParam("statusEnum") final SubmissionStatusEnum statusEnum,
                                           @PathParam("userId") final Long userId,
                                           @PathParam("token") final String token) {
        final Boolean isValid = userService.validateToken(userId,
                token);
        if (!isValid && userService.isAdmin(userId)) {
            Response.status(401)
                    .entity(AuthResponseStatusesEnum.VERIFICATION_NOT_VALID)
                    .build();
        }
        return Response.status(200)
                .entity(submissionService.getSubmissionsByStatus(statusEnum))
                .build();

    }

    @GET
    @Path("clean")
    public Response cleanIncompletedSubmittions() {
        return Response.status(200)
                .entity(submissionService.cleanIncompletedSubmissions())
                .build();

    }

    @PUT
    @Path("update/data/{adminId}/{adminToken}")
    public Response changeSubmissionStatus(@PathParam("adminId") final Long adminId,
                                           @PathParam("adminToken") final String adminToken,
                                           final SubmissionDTO dto) {
        final Boolean isValid = userService.validateToken(adminId,
                adminToken);
        if (!isValid && userService.isAdmin(adminId)) {
            Response.status(401)
                    .entity(AuthResponseStatusesEnum.VERIFICATION_NOT_VALID)
                    .build();
        }
        return Response.status(200)
                .entity(submissionService.updateEntireSubmissionAndParticipants(dto))
                .build();

    }

    @DELETE
    @Path("delete/uploads/{submissionUUID}/{userId}")
    public void deleteUploads(@PathParam("submissionUUID") final String submissionUUID, @PathParam("userId") final Long userId) {
        submissionService.deleteUploads(submissionUUID, userId);
    }


    @POST
    @Path("publish/{submissionId}/{adminId}/{adminToken}")
    public Response publishSubmission(@PathParam("adminId") final Long adminId,
                                      @PathParam("adminToken") final String adminToken,
                                      @PathParam("submissionId") final Long submissionId) {
        final Boolean isValid = userService.validateToken(adminId,
                adminToken);
        if (!isValid && userService.isAdmin(adminId)) {
            Response.status(401)
                    .entity(AuthResponseStatusesEnum.VERIFICATION_NOT_VALID)
                    .build();
        }
        return Response.status(200)
                .entity(submissionService.publishSubmission(submissionId))
                .build();

    }


    @GET
    @Path("get/{uuid}/{userId}/{adminId}/{adminToken}")
    public Response getSubmissionByUuidAsAdmin(@PathParam("uuid") final String uuid,
                                               @PathParam("userId") final Long userId,
                                               @PathParam("adminId") final Long adminId,
                                               @PathParam("adminToken") final String adminToken) {
        final Boolean isValid = userService.validateToken(adminId,
                adminToken);
        if (!isValid && userService.isAdmin(adminId)) {
            Response.status(401)
                    .entity(AuthResponseStatusesEnum.VERIFICATION_NOT_VALID)
                    .build();
        }
        return Response.status(200)
                .entity(submissionService.geSubmissionByUUid(uuid, userId))
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
