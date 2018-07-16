package yf.user.rest;

import yf.user.UserDao;
import yf.user.UserWorkflow;
import yf.user.dto.AuthResponseStatusesEnum;
import yf.user.dto.LoginDTO;
import yf.user.dto.ProfilePictureDTO;
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
import javax.ws.rs.PUT;
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

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUser(user);
        loginDTO.setPassword(password);

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

    @POST
    @Path("update/name/{userId}/{token}/{firstName}/{lastName}")
    public Response updateFirstLastName(@PathParam("userId") final Long userId,
                                        @PathParam("token") final String token,
                                        @PathParam("firstName") final String firstName,
                                        @PathParam("lastName") final String lastName) {
        final Boolean isValid = userService.validateToken(userId,
                token);
        if (!isValid) {
            Response.status(403).entity(AuthResponseStatusesEnum.TOKEN_NOT_VALID).build();
        }

        final User user = userService.updateUserFirstLastName(userId, firstName, lastName);
        final UserAllDataDto userSocialAccounts = userWorkflow.getUserSocialAccounts(user);
        Map<String, Object> resp = authRestHelper.userAuthResponseEntityMap(userSocialAccounts);

        return Response.status(200)
                .entity(resp)
                .build();
    }


    @POST
    @Path("update/nickname/{userId}/{token}/{nickname}")
    public Response updateFirstLastName(@PathParam("userId") final Long userId,
                                        @PathParam("token") final String token,
                                        @PathParam("nickname") final String nickname) {

        final Boolean isValid = userService.validateToken(userId,
                token);


        if (!isValid) {
            Response.status(403).entity(AuthResponseStatusesEnum.TOKEN_NOT_VALID).build();
        }

        final String lowerCaseNickname = nickname.toLowerCase();

        if (!lowerCaseNickname.matches("[a-zA-Z0-9]+") || lowerCaseNickname.length() < 4 || lowerCaseNickname.contains(" ")) {
            return Response.status(403).entity(AuthResponseStatusesEnum.NICKNAME_WRONG).build();
        }

        final User userByEmailNickName = userWorkflow.getUserByEmailNickName(lowerCaseNickname);

        if(userByEmailNickName != null && !userByEmailNickName.getId().equals(userId)) {
            return Response.status(403).entity(AuthResponseStatusesEnum.NICKNAME_ALREADY_EXIST).build();
        }

        final User user = userService.updateUserNickname(userId, lowerCaseNickname);
        final UserAllDataDto userSocialAccounts = userWorkflow.getUserSocialAccounts(user);
        Map<String, Object> resp = authRestHelper.userAuthResponseEntityMap(userSocialAccounts);

        return Response.status(200)
                .entity(resp)
                .build();
    }

    @PUT
    @Path("update/profilepic/{userId}/{token}")
    public Response updateFirstLastName(@PathParam("userId") final Long userId,
                                        @PathParam("token") final String token,
                                        final ProfilePictureDTO dto) {
        final Boolean isValid = userService.validateToken(userId,
                token);


        if (!isValid) {
            Response.status(403).entity(AuthResponseStatusesEnum.TOKEN_NOT_VALID).build();
        }

        userService.updateUserProfilePic(userId, dto);

        return Response.ok().build();
    }

}
