package yf.user.services;

import javax.inject.Inject;

import yf.user.dto.GeneralUserDTO;
import yf.user.dto.VKResponseDTO;
import yf.user.dto.VKUserDTO;
import yf.user.rest.UserRestClient;
import yf.user.workflow.UserWorkflow;

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
}
