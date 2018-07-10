package yf.user.rest;

import yf.user.UserWorkflow;
import yf.user.dto.LoginDTO;
import yf.user.dto.UserAllDataDto;
import yf.user.entities.User;
import yf.user.services.UserService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("auth")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class LoginRestImpl {

    @Inject
    private UserService userService;
    @Inject
    private UserWorkflow userWorkflow;
    @Inject
    private AuthRestHelper authRestHelper;

    @POST
    @Path("login")
    public Response login(final LoginDTO loginDTO) {
        final User user = userService.getUserByEmailNickName(loginDTO);

        Response errorResponse = authRestHelper.isAuthValid(loginDTO, user);

        if (errorResponse != null) {
            return errorResponse;
        }

        final UserAllDataDto userSocialAccounts = userWorkflow.getUserSocialAccounts(user);

        Map<String, Object> resp = authRestHelper.userAuthResponseEntityMap(userSocialAccounts);

        return Response.status(200)
                .entity(resp)
                .build();
    }

    @POST
    @Path("register")
    public Response register(final LoginDTO loginDTO) {
        final User user = userService.getUserByEmailNickName(loginDTO);

        Response errorResponse = authRestHelper.isRegistrationValid(loginDTO);

        if (errorResponse != null) {
            return errorResponse;
        }

        final User registeredUsed = userWorkflow.registerUser(loginDTO);
        final UserAllDataDto dto = userWorkflow.getUserSocialAccounts(registeredUsed);

        Map<String, Object> resp = authRestHelper.userAuthResponseEntityMap(dto);

        return Response.status(200)
                .entity(resp)
                .build();
    }

    @GET
    @Path("/reset/email/request/{email}")
    public Response requestResetPassword(@PathParam("email") String email) {
        return userService.requestResetPassword(email);
    }

    @GET
    @Path("reset/{verification}/{newPassword}/{repeatPassword}")
    public Response resetPassword(@PathParam("verification") String verification,
                                  @PathParam("newPassword") String newPassword,
                                  @PathParam("repeatPassword") String repeatPassword) {
        return userService.resetPassword(verification,
                newPassword,
                repeatPassword);
    }

    // verification of email, phone etc ...
    @GET
    @Path("verify/{verification}")
    public Response verifyUser(@PathParam("verification") String verification) {
        return userService.verifyUserVerification(verification);
    }

    @GET
    @Path("validate/token/{userId}/{token}")
    public Response validateToken(@PathParam("userId") Long userId,
                                  @PathParam("token") String token) {
        final Boolean isValid = userService.validateToken(userId,
                token);
        return Response.ok()
                .entity(isValid)
                .build();
    }

    @GET
    @Path("validate/{uuid}")
    public Response validateUIID(@PathParam("uuid") String uuid) {
        return userService.validateUiidVerification(uuid);
    }

}
