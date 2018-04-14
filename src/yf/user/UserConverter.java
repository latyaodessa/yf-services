package yf.user;

import yf.user.dto.UserDto;
import yf.user.dto.external.fb.FBUserDTO;
import yf.user.dto.external.vk.VKUserDTO;
import yf.user.entities.FBUser;
import yf.user.entities.User;
import yf.user.entities.VKUser;

public class UserConverter {


    public UserDto toBasicUserDto(final User entity) {
        UserDto dto = new UserDto();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setType(entity.getType());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    public VKUserDTO toVkUserDto(final VKUser entity) {

        VKUserDTO vkUserDTO = new VKUserDTO();

        vkUserDTO.setId(entity.getId());
        vkUserDTO.setFirstName(entity.getFirstName());
        vkUserDTO.setLastName(entity.getLastName());
        vkUserDTO.setSex(entity.getSex());
        vkUserDTO.setBdate(entity.getBdate());
        vkUserDTO.setMobilePhone(entity.getMobilePhone());
        vkUserDTO.setHomePhone(entity.getHomePhone());
        vkUserDTO.setVerified(entity.getVerified());
        vkUserDTO.setCity(entity.getCity());
        vkUserDTO.setCountry(entity.getCountry());


        return vkUserDTO;
    }


    public FBUserDTO toFbUserDto(final FBUser entity) {
        FBUserDTO dto = new FBUserDTO();

        dto.setId(entity.getId());
        dto.setFirst_name(entity.getFirst_name());
        dto.setLast_name(entity.getLast_name());
        dto.setBirthday(entity.getBirthday());
        dto.setLocale(entity.getLocale());
        dto.setLocation(entity.getLocation());
        dto.setGender(entity.getGender());
        dto.setHometown(entity.getHometown());

        final UserDto user = toBasicUserDto(entity.getUser());
        dto.setUserDto(user);

        return dto;
    }

    public VKUser toVKUsetEntity(final VKUserDTO dto, final User user) {
        VKUser vkUserEntity = new VKUser();

        vkUserEntity.setId(dto.getId());
        vkUserEntity.setUser(user);
        vkUserEntity.setFirstName(dto.getFirstName());
        vkUserEntity.setLastName(dto.getLastName());
        vkUserEntity.setSex(dto.getSex());
        vkUserEntity.setBdate(dto.getBdate());
        vkUserEntity.setMobilePhone(dto.getMobilePhone());
        vkUserEntity.setHomePhone(dto.getHomePhone());
        vkUserEntity.setVerified(dto.getVerified());
        vkUserEntity.setCity(dto.getCity());
        vkUserEntity.setCountry(dto.getCountry());

        return vkUserEntity;

    }

    public FBUser toFBUserEntity(final FBUserDTO fbUserDTO, final User user) {
        FBUser entity = new FBUser();

        entity.setId(fbUserDTO.getId());
        entity.setUser(user);

        entity.setFirst_name(fbUserDTO.getFirst_name());
        entity.setLast_name(fbUserDTO.getLast_name());
        entity.setLocation(fbUserDTO.getLocation());
        entity.setLocale(fbUserDTO.getLocale());
        entity.setGender(fbUserDTO.getGender());
        entity.setBirthday(fbUserDTO.getBirthday());
        entity.setHometown(fbUserDTO.getHometown());
        entity.setHometown(fbUserDTO.getHometown());

        return entity;
    }
}
