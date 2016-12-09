package yf.user.rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import yf.user.dto.GeneralUserDTO;
import yf.user.dto.external.fb.FBResponseDTO;
import yf.user.dto.external.vk.VKResponseDTO;
import yf.user.dto.save.UserPhotoSaveDataDTO;
import yf.user.dto.save.UserPostSaveDataDTO;
import yf.user.entities.usersaved.UserSavedPhotos;
import yf.user.entities.usersaved.UserSavedPosts;
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
	
	@POST
	@Path("fb/create")
	public FBResponseDTO createFBUser(FBResponseDTO fbResponseDTO){
		return userService.createFBUser(fbResponseDTO);
	}
	
	@POST
	@Path("save/post")
	public UserPostSaveDataDTO savePostForUser(UserPostSaveDataDTO post){
		return userService.saveNewPostForUser(post);
	}
	
	@POST
	@Path("save/photo")
	public UserPhotoSaveDataDTO savePhotoForUser(UserPhotoSaveDataDTO photoDTO){
		return userService.saveNewPhotoForUser(photoDTO);
	}
	
	@DELETE
	@Path("delete/photo")
	public UserSavedPhotos deletePhotoFromUser(@QueryParam("user_id") Long user_id, @QueryParam("photo_id") Long photo_id){
		return userService.deletePhotoFromUser(user_id, photo_id);
	}
	
	@GET
	@Path("save/post/{user_id}/{post_id}")
	public Boolean isPostAlreadySavedToUser(@PathParam("user_id") long user_id, @PathParam("post_id") long post_id){
		return userService.isPostAlreadySavedToUser(user_id, post_id);
	}
	@DELETE
	@Path("delete/post")
	public List <UserSavedPosts> deletePostFromUser(@QueryParam("user_id") Long user_id, @QueryParam("post_id") Long post_id){
		return userService.deletePostFromUser(user_id, post_id);
	}

}
