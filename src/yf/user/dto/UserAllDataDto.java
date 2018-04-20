package yf.user.dto;

import yf.user.dto.external.FBUserDTO;
import yf.user.dto.external.VKUserDTO;

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

    public void setVkUser(final VKUserDTO vkUser) {
        this.vkUser = vkUser;
    }

    public FBUserDTO getFbUser() {
        return fbUser;
    }

    public void setFbUser(final FBUserDTO fbUser) {
        this.fbUser = fbUser;
    }

    private UserDto user;
    private VKUserDTO vkUser;
    private FBUserDTO fbUser;
}
