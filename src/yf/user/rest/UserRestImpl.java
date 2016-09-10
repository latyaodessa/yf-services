package yf.user.rest;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import yf.user.dto.GeneralUserDTO;
import yf.user.dto.VKResponseDTO;
import yf.user.services.UserService;

@Path("user")
@Produces(MediaType.APPLICATION_JSON)
@Stateless 
public class UserRestImpl {
	@Inject
	UserService userService;
	
	@GET
	@Path("get/{id}")
	public GeneralUserDTO getUserById(@PathParam("id") long userId){
		return userService.getUserById(userId);
	}
	
	@POST
	@Path("vk/create/{id}")
	public VKResponseDTO createVKUser(@PathParam("id") long userId){
		return userService.createVKUser(userId);
	}

}
