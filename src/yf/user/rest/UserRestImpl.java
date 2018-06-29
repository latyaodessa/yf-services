package yf.user.rest;

import yf.user.UserDao;
import yf.user.UserWorkflow;
import yf.user.dto.LoginDTO;
import yf.user.dto.UserAllDataDto;
import yf.user.dto.UserDto;
import yf.user.dto.external.FBUserDTO;
import yf.user.entities.FBUser;
import yf.user.entities.User;
import yf.user.entities.VKUser;
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

@Path("user")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class UserRestImpl {
    @Inject
    private UserService userService;
    @Inject
    private AuthRestHelper authRestHelper;
    @Inject
    private UserWorkflow userWorkflow;
    @Inject
    private UserDao userDao;

    @GET
    @Path("{id}")
    public UserAllDataDto getUserById(@PathParam("id") final Long userId) {
        return userService.getUserById(userId);
    }

    @GET
    @Path("basic/{id}")
    public UserDto getBasicUserById(@PathParam("id") final Long userId) {
        return userService.getBasicUserById(userId);
    }

    @GET
    @Path("vk/{social_id}/")
    public Response getVkUser(@PathParam("social_id") final Long socialId) {
        UserAllDataDto dto = userWorkflow.getUserByVkSocialId(socialId);
        Response errorResponse = authRestHelper.isSocialAuthValid(dto);

        if (errorResponse != null) {
            return errorResponse;
        }

        Map<String, Object> resp = authRestHelper.userAuthResponseEntityMap(dto);

        return Response.status(200)
                .entity(resp)
                .build();
    }

    @GET
    @Path("fb/{social_id}/")
    public Response getFbUser(@PathParam("social_id") final Long socialId) {
        final UserAllDataDto dto = userWorkflow.getUserByFbSocialId(socialId);
        Response errorResponse = authRestHelper.isSocialAuthValid(dto);

        if (errorResponse != null) {
            return errorResponse;
        }
        Map<String, Object> resp = authRestHelper.userAuthResponseEntityMap(dto);

        return Response.status(200)
                .entity(resp)
                .build();
    }


    @POST
    @Path("vk/create/{id}")
    public Response createVKUser(@PathParam("id") final long socialId, final LoginDTO loginDTO) {

        final VKUser vkUser = userDao.getVkUser(socialId);

        Response errorResponse = authRestHelper.isRegistrationValid(loginDTO);

        if (errorResponse != null) {
            return errorResponse;
        }


        final UserAllDataDto dto = userService.registerUserFromVKUser(socialId, loginDTO, vkUser);
        Map<String, Object> resp = authRestHelper.userAuthResponseEntityMap(dto);

        return Response.status(200)
                .entity(resp)
                .build();
    }


    @POST
    @Path("fb/create/{user}/{password}")
    public Response createFBUser(final FBUserDTO fbUserDTO, @PathParam("user") final String user,
                                 @PathParam("password") final String password) {

        LoginDTO loginDTO = new LoginDTO(user, password);

        final FBUser fbUser = userDao.getFbUser(fbUserDTO.getId());

        Response errorResponse = authRestHelper.isRegistrationValid(loginDTO);

        if (errorResponse != null) {
            return errorResponse;
        }

        final UserAllDataDto dto = userService.registerUserFromFBUser(fbUserDTO, loginDTO, fbUser);
        Map<String, Object> resp = authRestHelper.userAuthResponseEntityMap(dto);

        return Response.status(200)
                .entity(resp)
                .build();
    }

}
