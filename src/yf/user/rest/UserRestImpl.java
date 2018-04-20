package yf.user.rest;

import yf.user.dto.UserAllDataDto;
import yf.user.dto.UserDto;
import yf.user.dto.external.FBUserDTO;
import yf.user.dto.external.VKUserDTO;
import yf.user.services.UserService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("user")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class UserRestImpl {
    @Inject
    private UserService userService;

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
    public VKUserDTO getVkUser(@PathParam("social_id") final Long socialId) {
        return userService.getVkUser(socialId);
    }

    @GET
    @Path("fb/{social_id}/")
    public FBUserDTO getFbUser(@PathParam("social_id") final Long socialId) {
        return userService.getFbUser(socialId);
    }


    @POST
    @Path("vk/create/{id}")
    public VKUserDTO createVKUser(@PathParam("id") final long userId) {
        return userService.createVKUser(userId);
    }

    @POST
    @Path("fb/create")
    public FBUserDTO createFBUser(final FBUserDTO fbUserDTO) {
        return userService.createFBUser(fbUserDTO);
    }

}
