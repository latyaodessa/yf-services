package yf.user.services;

import javax.inject.Inject;

import yf.user.UserWorkflow;
import yf.user.dto.UserElasticDTO;
import yf.user.dto.external.fb.FBResponseDTO;
import yf.user.dto.external.vk.VKUserDTO;
import yf.user.rest.VkRestClient;

public class UserService {
	@Inject
	UserWorkflow userWorkflow;
	@Inject
	VkRestClient userRestClient;
	
	public UserElasticDTO getUserById(String userId){
		return userWorkflow.getUserById(userId);
	}
	
	public UserElasticDTO createVKUser(long userId){
		VKUserDTO vKUserDTO = userRestClient.getVKUserDetails(userId);
		return userWorkflow.saveVKUser(vKUserDTO);
	}
	
	public UserElasticDTO createFBUser(FBResponseDTO fbResponseDTO){
		return userWorkflow.saveFBUser(fbResponseDTO);
	}
	
}
