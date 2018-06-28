package yf.user;

import yf.user.dto.LoginDTO;
import yf.user.dto.UserAllDataDto;
import yf.user.dto.UserDto;
import yf.user.dto.UserStatusEnum;
import yf.user.dto.UserTypeEnum;
import yf.user.dto.VerificationTypesEnum;
import yf.user.dto.external.FBUserDTO;
import yf.user.dto.external.VKUserDTO;
import yf.user.entities.FBUser;
import yf.user.entities.User;
import yf.user.entities.UserVerifications;
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

    public UserAllDataDto getUserByVkSocialId(final Long socialId) {

        final VKUser vkUser = userGetDao.getVkUser(socialId);

        if (vkUser == null) {
            return null;
        }

        final User user = vkUser.getUser();
        UserAllDataDto userAllDataDto = new UserAllDataDto();
        userAllDataDto.setUser(
                userConverter.toBasicUserDto(user));
        userAllDataDto.setVkUser(userConverter.toVkUserDto(vkUser));
        userAllDataDto.setFbUser(getFbUserByUserId(user.getId()));

        return userAllDataDto;
    }

    public UserAllDataDto getUserByFbSocialId(final Long socialId) {

        final FBUser fbUser = userGetDao.getFbUser(socialId);

        if (fbUser == null) {
            return null;
        }

        final User user = fbUser.getUser();
        UserAllDataDto userAllDataDto = new UserAllDataDto();
        userAllDataDto.setUser(
                userConverter.toBasicUserDto(user));
        userAllDataDto.setFbUser(userConverter.toFbUserDto(fbUser));
        userAllDataDto.setVkUser(getVkUserByUserId(user.getId()));

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

    private User generateBasicUser() {
        User user = new User();
        user.setAuthorize(false);
        user.setCreatedOn(new Date());
        user.setStatus(UserStatusEnum.ACTIVE);
        user.setType(UserTypeEnum.BASIC);
        return user;
    }

    public VKUserDTO saveAndVerifyVKUser(final VKUserDTO vKUserDTO, final User user) {

//        VKUserDTO existingVkUser = getVkUser(vKUserDTO.getId());
//
//        if (existingVkUser != null) {
//            return existingVkUser;
//        }
//
//        em.persist(user);

        //TODO

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


    public UserAllDataDto getUserSocialAccounts(final User user) {

        if (user != null) {
            UserAllDataDto userAllDataDto = new UserAllDataDto();
            userAllDataDto.setUser(userConverter.toBasicUserDto(user));
            userAllDataDto.setVkUser(getVkUserByUserId(user.getId()));
            userAllDataDto.setFbUser(getFbUserByUserId(user.getId()));
            return userAllDataDto;
        }

        return null;
    }

    public User getUserByEmailNickName(final String emailOrNickname) {
        return userGetDao.getUserByNicknameOrEmail(emailOrNickname);
    }

    public User registerUser(final LoginDTO loginDTO) {
        UserVerifications verifications = UserVerifications.generateEmptyVerification();
        em.persist(verifications);

        User user = generateBasicUser();
        user.setEmail(loginDTO.getUser());
        user.setPassword(loginDTO.getPassword());
        user.setVerifications(verifications);
        em.persist(user);
        return user;
    }

    public User changeUserPassword(final Long userId, final String password) {
        final User user = userGetDao.getUserById(userId);
        if (user == null) {
            return null;
        }
        user.setPassword(password);
        em.persist(user);
        return user;

    }

    public User verifyUserVerification(final Long userId, final VerificationTypesEnum verificationTypesEnum) {
        final User user = userGetDao.getUserById(userId);
        if (user == null) {
            return null;
        }
        final UserVerifications userVerifications = user.getVerifications();

        switch (verificationTypesEnum) {
            case EMAIL:
                userVerifications.setEmail(true);
                break;
            case PHONE:
                userVerifications.setPhone(true);
                break;
            default:
                break;

        }
        em.persist(userVerifications);
        em.persist(user);
        return user;

    }


}
