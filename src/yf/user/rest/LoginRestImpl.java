package yf.user.rest;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import yf.user.UserWorkflow;
import yf.user.dto.AuthResponseStatusesEnum;
import yf.user.dto.LoginDTO;
import yf.user.dto.UserAllDataDto;
import yf.user.entities.User;
import yf.user.services.JWTService;
import yf.user.services.UserService;

@Path("auth")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class LoginRestImpl {

    @Inject
    private UserService userService;
    @Inject
    private JWTService jwtService;
    @Inject
    private UserWorkflow userWorkflow;

    @POST
    @Path("login")
    public Response login(final LoginDTO loginDTO) {
        return userService.getUserByEmailNickNameAndPassword(loginDTO);
    }

    @POST
    @Path("register")
    public Response register(final LoginDTO loginDTO) {
        final User user = userService.getUserByEmailNickName(loginDTO);

        AuthResponseStatusesEnum error = userService.newUserValidityCheck(user,
                loginDTO);

        if (error != null) {
            return Response.status(401)
                    .entity(error)
                    .build();
        }

        final User registeredUsed = userService.registerUser(loginDTO);
        final UserAllDataDto dto = userWorkflow.getUserSocialAccounts(registeredUsed);

        Map<String, Object> resp = new HashMap<>();
        resp.put("user",
                dto);
        resp.put("token",
                jwtService.createToken(dto.getUser()));

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
        return userService.validateToken(userId,
                token);
    }

    @GET
    @Path("validate/{uuid}")
    public Response validateUIID(@PathParam("uuid") String uuid) {
        return userService.validateUiidVerification(uuid);
    }

}
