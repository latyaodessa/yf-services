package yf.user.services;

import yf.mail.services.EmailService;
import yf.user.UserWorkflow;
import yf.user.dto.AuthResponseStatusesEnum;
import yf.user.dto.LoginDTO;
import yf.user.dto.ProfilePictureDTO;
import yf.user.dto.UserAllDataDto;
import yf.user.dto.UserDto;
import yf.user.dto.external.FBUserDTO;
import yf.user.dto.external.VKUserDTO;
import yf.user.entities.FBUser;
import yf.user.entities.User;
import yf.user.entities.VKUser;
import yf.user.entities.Verifications;
import yf.user.rest.VkRestClient;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.Response;

public class UserService {
    @Inject
    private UserWorkflow userWorkflow;
    @Inject
    private VkRestClient userRestClient;
    @Inject
    private EmailService emailService;
    @Inject
    private JWTService jwtService;
    @Inject
    private VerificationService verificationService;
    @PersistenceContext
    private EntityManager em;

    public UserAllDataDto getUserById(final Long userId) {
        return userWorkflow.getUserById(userId);
    }

    public UserDto getBasicUserById(final Long userId) {
        return userWorkflow.getBasicUserById(userId);
    }

    public UserAllDataDto registerUserFromVKUser(final long userId, final LoginDTO loginDTO, final VKUser vkUser) {
        User user;
        final VKUserDTO vKUserDTO = userRestClient.getVKUserDetails(userId);

        if (vkUser == null) {
            user = userWorkflow.registerUser(loginDTO);
            user.setFirstName(vKUserDTO.getFirstName());
            user.setLastName(vKUserDTO.getLastName());
            em.persist(user);
            final VKUser vkUserEntity = userWorkflow.createVKUser(vKUserDTO, user);
            return userWorkflow.getUserSocialAccounts(user, vkUserEntity);
        } else {
            user = userWorkflow.registerExistingUser(vkUser.getUser(), loginDTO);
            user.setFirstName(vKUserDTO.getFirstName());
            user.setLastName(vKUserDTO.getLastName());
            em.persist(user);
            return userWorkflow.getUserSocialAccounts(user, vkUser);
        }
    }

    public void bindVkAccountToUser(final User user, final VKUser vkuser, final Long vkId) {

        if (vkuser == null) {
            final VKUserDTO vKUserDTO = userRestClient.getVKUserDetails(vkId);
            final VKUser vkUserEntity = userWorkflow.createVKUser(vKUserDTO, user);
            userWorkflow.handleEmptyUserFields(user, vkUserEntity);
        } else {
            userWorkflow.handleEmptyUserFields(user, vkuser);
            vkuser.setUser(user);
            em.merge(vkuser);
        }

    }

    public void unbindVkAccountToUser(final VKUser vkuser) {

        if (vkuser != null) {
            vkuser.setUser(null);
            em.merge(vkuser);
        }
    }

    public UserAllDataDto registerUserFromFBUser(final FBUserDTO fbUserDTO, final LoginDTO loginDTO, final FBUser fbUser) {

        User user;

        if (fbUser == null) {
            user = userWorkflow.registerUser(loginDTO);
            user.setGender(fbUserDTO.getGender());
            user.setFirstName(fbUserDTO.getFirst_name());
            user.setLastName(fbUserDTO.getLast_name());
            em.persist(user);
            final FBUser fbUserEntity = userWorkflow.createFBUser(fbUserDTO, user);
            return userWorkflow.getUserSocialAccounts(user, fbUserEntity);
        } else {
            user = userWorkflow.registerExistingUser(fbUser.getUser(), loginDTO);
            user.setFirstName(fbUserDTO.getFirst_name());
            user.setLastName(fbUserDTO.getLast_name());
            user.setGender(fbUserDTO.getGender());
            em.persist(user);
            return userWorkflow.getUserSocialAccounts(user, fbUser);
        }

    }

    public User getUserByEmailNickName(final LoginDTO loginDTO) {
        return userWorkflow.getUserByEmailNickName(loginDTO.getUser());
    }

    public Boolean validateToken(final Long userId,
                                 final String token) {
        return jwtService.isValidToken(token,
                userId);

    }

    public Response validateUiidVerification(final String verification) {
        final Verifications ver = verificationService.getVerification(verification);
        if (ver == null) {
            return Response.status(401)
                    .entity(AuthResponseStatusesEnum.VERIFICATION_NOT_VALID)
                    .build();
        }
        return Response.status(200)
                .entity(ver)
                .build();
    }

    public Response requestResetPassword(final String email) {
        final User user = userWorkflow.getUserByEmailNickName(email);
        if (user == null) {
            return Response.status(401)
                    .entity(AuthResponseStatusesEnum.NOT_EXIST)
                    .build();
        }

        emailService.sendResetPasswordEmail(user);

        return Response.ok()
                .build();
    }

    public Response resetPassword(final String verification,
                                  final String newPassword,
                                  final String repeatPassword) {

        if (newPassword.length() < 7 || newPassword.matches("\\s+") || repeatPassword.matches("\\s+")) {
            return Response.status(401)
                    .entity(AuthResponseStatusesEnum.PASSWORD_NOT_VALID)
                    .build();
        }

        if (!newPassword.equals(repeatPassword)) {
            return Response.status(401)
                    .entity(AuthResponseStatusesEnum.PASSWORDS_NOT_MATCING)
                    .build();

        }

        final Verifications verifications = verificationService.validateVerification(verification);

        if (verifications == null) {
            return Response.status(401)
                    .entity(AuthResponseStatusesEnum.VERIFICATION_NOT_VALID)
                    .build();
        }

        final User user = userWorkflow.changeUserPassword(verifications.getUserId(),
                newPassword);

        if (user == null) {
            return Response.status(401)
                    .entity(AuthResponseStatusesEnum.NOT_EXIST)
                    .build();
        }

        return Response.ok()
                .build();
    }

    public Response verifyUserVerification(final String verification) {
        final Verifications ver = verificationService.validateVerification(verification);
        if (ver == null) {
            return Response.status(401)
                    .entity(AuthResponseStatusesEnum.VERIFICATION_NOT_VALID)
                    .build();
        }

        userWorkflow.verifyUserVerification(ver.getUserId(),
                ver.getType());

        return Response.ok()
                .build();
    }

    public User updateUserFirstLastName(final Long userId,
                                        final String firstName,
                                        final String lastName) {
        return userWorkflow.updateUserFirstLastName(userId, firstName, lastName);
    }

    public User updateUserNickname(final Long userId,
                                   final String nickname) {
        return userWorkflow.updateNickName(userId, nickname);
    }

    public void updateUserProfilePic(final Long userId, final ProfilePictureDTO dto) {
        userWorkflow.updateUserProfilePic(userId, dto);
    }
}
