package yf.user.services;

import java.util.List;

import javax.inject.Inject;

import yf.user.UserWorkflow;
import yf.user.dto.GeneralUserDTO;
import yf.user.dto.external.fb.FBResponseDTO;
import yf.user.dto.external.vk.VKResponseDTO;
import yf.user.dto.external.vk.VKUserDTO;
import yf.user.dto.save.UserPhotoSaveDataDTO;
import yf.user.dto.save.UserPostSaveDataDTO;
import yf.user.entities.usersaved.UserSavedPhotos;
import yf.user.entities.usersaved.UserSavedPosts;
import yf.user.rest.UserRestClient;

public class UserService {
	@Inject
	UserWorkflow userWorkflow;
	@Inject
	UserRestClient userRestClient;
	
	public GeneralUserDTO getUserById(long userId){
		return userWorkflow.getUserById(userId);
	}
	
	public VKResponseDTO createVKUser(long userId){
		VKUserDTO vKUserDTO = userRestClient.getVKUserDetails(userId);
		return userWorkflow.saveVKUser(vKUserDTO);
	}
	
	public FBResponseDTO createFBUser(FBResponseDTO fbResponseDTO){
		return userWorkflow.saveFBUser(fbResponseDTO);
	}
	
	public UserPostSaveDataDTO saveNewPostForUser(UserPostSaveDataDTO post){
		return userWorkflow.saveNewPostForUser(post);
	}
	
	public UserPhotoSaveDataDTO saveNewPhotoForUser(UserPhotoSaveDataDTO postDTO){
		return userWorkflow.saveNewPhotoForUser(postDTO);
	}
	
	public Boolean isPostAlreadySavedToUser(long user_id, long post_id){
		return userWorkflow.isPostAlreadySavedToUser(user_id, post_id);
	}
	public List <UserSavedPosts> deletePostFromUser(Long user_id, Long post_id){
		return userWorkflow.deletePostFromUser(user_id, post_id);
	}
	public UserSavedPhotos deletePhotoFromUser(Long user_id, Long photo_id){
		return userWorkflow.deletePhotoFromUser(user_id, photo_id);
	}
	
}
