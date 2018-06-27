package yf.user.rest;

import yf.user.dto.LoginDTO;
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

@Path("auth")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class LoginRestImpl {

    @Inject
    private UserService userService;

    @POST
    @Path("login")
    public Response login(final LoginDTO loginDTO) {
        return userService.getUserByEmailNickNameAndPassword(loginDTO);
    }

    @POST
    @Path("register")
    public Response register(final LoginDTO loginDTO) {
        return userService.registerUser(loginDTO);
    }

    @GET
    @Path("/reset/email/request/{email}")
    public Response requestResetPassword(@PathParam("email") String email) {
        return userService.requestResetPassword(email);
    }

    @GET
    @Path("reset/{verification}/{newPassword}/{repeatPassword}")
    public Response resetPassword(@PathParam("verification") String verification, @PathParam("newPassword") String newPassword, @PathParam("repeatPassword") String repeatPassword) {
        return userService.resetPassword(verification, newPassword, repeatPassword);
    }

    // verification of email, phone etc ...
    @GET
    @Path("verify/{verification}")
    public Response verifyUser(@PathParam("verification") String verification) {
        return userService.verifyUserVerification(verification);
    }


    @GET
    @Path("validate/token/{userId}/{token}")
    public Response validateToken(@PathParam("userId") Long userId, @PathParam("token") String token) {
        return userService.validateToken(userId, token);
    }

    @GET
    @Path("validate/{uuid}")
    public Response validateUIID(@PathParam("uuid") String uuid) {
        return userService.validateUiidVerification(uuid);
    }

}
