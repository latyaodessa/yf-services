package yf.user;

import yf.dashboard.postphoto.entities.UserSavedPosts;
import yf.user.dto.UserAllDataDto;
import yf.user.dto.UserDto;
import yf.user.dto.UserStatusEnum;
import yf.user.dto.UserTypeEnum;
import yf.user.dto.external.fb.FBUserDTO;
import yf.user.dto.external.vk.VKUserDTO;
import yf.user.entities.FBUser;
import yf.user.entities.User;
import yf.user.entities.VKUser;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

@Stateless
public class UserWorkflow {

    @PersistenceContext
    private EntityManager em;
    @Inject
    private UserConverter userConverter;

    @Inject
    private UserGetDao userGetDao;

    public UserAllDataDto getUserById(final Long userId) {
        UserAllDataDto userAllDataDto = new UserAllDataDto();
        userAllDataDto.setUser(getBasicUserById(userId));
        userAllDataDto.setVkUser(getVkUserByUserId(userId));
        userAllDataDto.setFbUser(getFbUserByUserId(userId));
        return userAllDataDto;

    }

    public UserDto getBasicUserById(final Long userId) {
        final User user = userGetDao.getUserById(userId);
        return user != null ?
                userConverter.toBasicUserDto(user)
                : null;
    }

    public VKUserDTO getVkUser(final Long socialId) {
        final VKUser vkUser = userGetDao.getVkUser(socialId);
        return vkUser != null ?
                userConverter.toVkUserDto(vkUser)
                : null;
    }

    private VKUserDTO getVkUserByUserId(final Long userId) {
        final VKUser vkUser = userGetDao.getVkUserByUserId(userId);
        return vkUser != null ?
                userConverter.toVkUserDto(vkUser)
                : null;
    }

    public FBUserDTO getFbUser(final Long socialId) {
        final FBUser fbUser = userGetDao.getFbUser(socialId);
        return fbUser != null ?
                userConverter.toFbUserDto(fbUser)
                : null;
    }

    private FBUserDTO getFbUserByUserId(final Long userId) {
        final FBUser fbUser = userGetDao.getFbUserByUserId(userId);
        return fbUser != null ?
                userConverter.toFbUserDto(fbUser)
                : null;
    }


    public UserSavedPosts getSavedUserPostById(long postId) {
        UserSavedPosts userSavedPosts = em.find(UserSavedPosts.class, postId);
        return userSavedPosts;

    }

    public User generateBasicUser() {
        User user = new User();
        user.setAuthorize(false);
        user.setCreatedOn(new Date());
        user.setStatus(UserStatusEnum.ACTIVE);
        user.setType(UserTypeEnum.BASIC);
        return user;
    }

    public VKUserDTO saveAndVerifyVKUser(final VKUserDTO vKUserDTO) {

        VKUserDTO existingVkUser = getVkUser(vKUserDTO.getId());

        if (existingVkUser != null) {
            return existingVkUser;
        }

        User user = generateBasicUser();
        em.persist(user);

        VKUser vkUserEntity = userConverter.toVKUsetEntity(vKUserDTO, user);
        em.persist(vkUserEntity);

        vKUserDTO.setUserDto(userConverter.toBasicUserDto(user));

        return vKUserDTO;

    }

    public FBUserDTO saveFBUser(final FBUserDTO fbUserDTO) {

        FBUserDTO existingFbUser = getFbUser(fbUserDTO.getId());

        if (existingFbUser != null) {
            return existingFbUser;
        }

        User user = generateBasicUser();
        em.persist(user);

        FBUser fbUserEntity = userConverter.toFBUserEntity(fbUserDTO, user);
        em.persist(fbUserEntity);

        fbUserDTO.setUserDto(userConverter.toBasicUserDto(user));
        return fbUserDTO;

    }


}
