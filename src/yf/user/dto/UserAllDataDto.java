package yf.user.dto;

import yf.user.dto.external.fb.FBUserDTO;
import yf.user.dto.external.vk.VKUserDTO;

public class UserAllDataDto {
    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public VKUserDTO getVkUser() {
        return vkUser;
    }

    public void setVkUser(VKUserDTO vkUser) {
        this.vkUser = vkUser;
    }

    public FBUserDTO getFbUser() {
        return fbUser;
    }

    public void setFbUser(FBUserDTO fbUser) {
        this.fbUser = fbUser;
    }

    private UserDto user;
    private VKUserDTO vkUser;
    private FBUserDTO fbUser;
}
