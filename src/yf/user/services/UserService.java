package yf.user.services;

import yf.user.UserWorkflow;
import yf.user.dto.UserAllDataDto;
import yf.user.dto.UserDto;
import yf.user.dto.external.fb.FBUserDTO;
import yf.user.dto.external.vk.VKUserDTO;
import yf.user.rest.VkRestClient;

import javax.inject.Inject;

public class UserService {
    @Inject
    private UserWorkflow userWorkflow;
    @Inject
    private VkRestClient userRestClient;

    public UserAllDataDto getUserById(final Long userId) {
        return userWorkflow.getUserById(userId);
    }


    public UserDto getBasicUserById(final Long userId) {
        return userWorkflow.getBasicUserById(userId);
    }

    public VKUserDTO getVkUser(final Long socialId) {
        return userWorkflow.getVkUser(socialId);

    }

    public FBUserDTO getFbUser(final Long socialId) {
        return userWorkflow.getFbUser(socialId);

    }

    public VKUserDTO createVKUser(final long userId) {
        VKUserDTO vKUserDTO = userRestClient.getVKUserDetails(userId);
        return userWorkflow.saveAndVerifyVKUser(vKUserDTO);
    }

    public FBUserDTO createFBUser(final FBUserDTO fbUserDTO) {
        return userWorkflow.saveFBUser(fbUserDTO);
    }

}
