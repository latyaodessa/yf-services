package yf.user;

import java.util.Date;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import yf.mail.services.EmailService;
import yf.user.dto.LoginDTO;
import yf.user.dto.ProfilePictureDTO;
import yf.user.dto.UserAllDataDto;
import yf.user.dto.UserDto;
import yf.user.dto.UserStatusEnum;
import yf.user.dto.UserTypeEnum;
import yf.user.dto.VerificationTypesEnum;
import yf.user.dto.external.FBUserDTO;
import yf.user.dto.external.VKUserDTO;
import yf.user.entities.FBUser;
import yf.user.entities.ProfilePicture;
import yf.user.entities.User;
import yf.user.entities.UserVerifications;
import yf.user.entities.VKUser;

@Stateless
public class UserWorkflow {

    @PersistenceContext
    private EntityManager em;
    @Inject
    private UserConverter userConverter;
    @Inject
    private EmailService emailService;
    @Inject
    private UserDao userDao;
    @Inject
    private ProfilePictureConverter profilePictureConverter;

    public UserAllDataDto getUserById(final Long userId) {
        UserAllDataDto userAllDataDto = new UserAllDataDto();
        userAllDataDto.setUser(getBasicUserById(userId));
        userAllDataDto.setVkUser(getVkUserByUserId(userId));
        userAllDataDto.setFbUser(getFbUserByUserId(userId));
        return userAllDataDto;

    }

    public UserAllDataDto getUserByVkSocialId(final Long socialId) {

        final VKUser vkUser = userDao.getVkUser(socialId);

        if (vkUser == null) {
            return null;
        }

        final User user = vkUser.getUser();
        UserAllDataDto userAllDataDto = new UserAllDataDto();
        userAllDataDto.setUser(userConverter.toBasicUserDto(user));
        userAllDataDto.setVkUser(userConverter.toVkUserDto(vkUser));
        userAllDataDto.setFbUser(getFbUserByUserId(user.getId()));

        return userAllDataDto;
    }

    public UserAllDataDto getUserByFbSocialId(final Long socialId) {

        final FBUser fbUser = userDao.getFbUser(socialId);

        if (fbUser == null) {
            return null;
        }

        final User user = fbUser.getUser();
        UserAllDataDto userAllDataDto = new UserAllDataDto();
        userAllDataDto.setUser(userConverter.toBasicUserDto(user));
        userAllDataDto.setFbUser(userConverter.toFbUserDto(fbUser));
        userAllDataDto.setVkUser(getVkUserByUserId(user.getId()));

        return userAllDataDto;
    }

    public UserDto getBasicUserById(final Long userId) {
        final User user = userDao.getUserById(userId);
        return user != null ? userConverter.toBasicUserDto(user)
                            : null;
    }

    public VKUserDTO getVkUser(final Long socialId) {
        final VKUser vkUser = userDao.getVkUser(socialId);
        return vkUser != null ? userConverter.toVkUserDto(vkUser)
                              : null;
    }

    private VKUserDTO getVkUserByUserId(final Long userId) {
        final VKUser vkUser = userDao.getVkUserByUserId(userId);
        return vkUser != null ? userConverter.toVkUserDto(vkUser)
                              : null;
    }

    public FBUserDTO getFbUser(final Long socialId) {
        final FBUser fbUser = userDao.getFbUser(socialId);
        return fbUser != null ? userConverter.toFbUserDto(fbUser)
                              : null;
    }

    private FBUserDTO getFbUserByUserId(final Long userId) {
        final FBUser fbUser = userDao.getFbUserByUserId(userId);
        return fbUser != null ? userConverter.toFbUserDto(fbUser)
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

    public VKUser createVKUser(final VKUserDTO vKUserDTO,
                               final User user) {
        VKUser entity = userConverter.toVKUsetEntity(vKUserDTO,
                user);
        em.persist(entity);
        em.flush();
        return entity;

    }

    public FBUser createFBUser(final FBUserDTO fbUserDTO,
                               final User user) {
        FBUser fbUserEntity = userConverter.toFBUserEntity(fbUserDTO,
                user);
        em.persist(fbUserEntity);
        return fbUserEntity;
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

    public UserAllDataDto getUserSocialAccounts(final User user,
                                                final VKUser vkUser) {

        if (user != null) {
            UserAllDataDto userAllDataDto = new UserAllDataDto();
            userAllDataDto.setUser(userConverter.toBasicUserDto(user));
            userAllDataDto.setVkUser(userConverter.toVkUserDto(vkUser));
            userAllDataDto.setFbUser(getFbUserByUserId(user.getId()));
            return userAllDataDto;
        }

        return null;
    }

    public UserAllDataDto getUserSocialAccounts(final User user,
                                                final FBUser fbUser) {

        if (user != null) {
            UserAllDataDto userAllDataDto = new UserAllDataDto();
            userAllDataDto.setUser(userConverter.toBasicUserDto(user));
            userAllDataDto.setVkUser(getVkUserByUserId(user.getId()));
            userAllDataDto.setFbUser(userConverter.toFbUserDto(fbUser));
            return userAllDataDto;
        }

        return null;
    }

    public User getUserByEmailNickName(final String emailOrNickname) {
        return userDao.getUserByNicknameOrEmail(emailOrNickname);
    }

    public User registerUser(final LoginDTO loginDTO) {
        UserVerifications verifications = UserVerifications.generateEmptyVerification();
        em.persist(verifications);

        User user = generateBasicUser();
        user.setEmail(loginDTO.getUser());
        user.setPassword(loginDTO.getPassword());
        user.setVerifications(verifications);
        user.setAuthorize(true);
        em.persist(user);

        emailService.sendVerificationEmail(user);

        return user;
    }

    public User registerAnonymousUser() {
        UserVerifications verifications = UserVerifications.generateEmptyVerification();
        em.persist(verifications);
        User user = generateBasicUser();
        user.setVerifications(verifications);
        em.persist(user);
        em.flush();
        return user;
    }

    public void handleEmptyUserFields(final User user,
                                      final VKUser vkUser) {
        if (user.getFirstName() == null) {
            user.setFirstName(vkUser.getFirstName());
        }
        if (user.getLastName() == null) {
            user.setLastName(vkUser.getLastName());
        }
        if (user.getGender() == null) {
            user.setGender(vkUser.getSex());
        }
        em.merge(user);
        em.flush();
    }

    public User registerExistingUser(final User user,
                                     final LoginDTO loginDTO) {
        UserVerifications userVerifications = user.getVerifications();

        if (userVerifications == null) {
            userVerifications = UserVerifications.generateEmptyVerification();
        }

        user.setEmail(loginDTO.getUser());
        user.setPassword(loginDTO.getPassword());
        user.setVerifications(userVerifications);
        user.setAuthorize(true);
        em.persist(userVerifications);
        em.persist(user);

        emailService.sendVerificationEmail(user);

        return user;
    }

    public User changeUserPassword(final Long userId,
                                   final String password) {
        final User user = userDao.getUserById(userId);
        if (user == null) {
            return null;
        }
        user.setPassword(password);
        em.persist(user);
        return user;

    }

    public User verifyUserVerification(final Long userId,
                                       final VerificationTypesEnum verificationTypesEnum) {
        final User user = userDao.getUserById(userId);
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

    public User updateUserFirstLastName(final Long userId,
                                        final String firstName,
                                        final String lastName) {
        User user = userDao.getUserById(userId);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        em.merge(user);
        return user;
    }

    public User updateNickName(final Long userId,
                               final String nickname) {
        User user = userDao.getUserById(userId);
        user.setNickName(nickname);
        em.merge(user);
        return user;

    }

    public void updateUserProfilePic(final Long userId,
                                     final ProfilePictureDTO dto) {
        User user = userDao.getUserById(userId);
        if (user == null) {
            return;
        }

        final Long oldProfileId = Optional.ofNullable(user.getProfilePicture())
                .map(ProfilePicture::getId)
                .orElse(null);

        if (oldProfileId != null) {

            ProfilePicture profilePictureOld = em.find(ProfilePicture.class,
                    oldProfileId);

            if (profilePictureOld != null) {
                user.setProfilePicture(null);
                em.merge(user);
                em.remove(profilePictureOld);
            }
        }

        ProfilePicture profilePictureEntity = profilePictureConverter.toEntity(dto);
        em.persist(profilePictureEntity);

        user.setProfilePicture(profilePictureEntity);
        em.merge(user);

    }

}
