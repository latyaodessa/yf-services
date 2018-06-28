package yf.user.services;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import yf.mail.services.EmailService;
import yf.user.UserWorkflow;
import yf.user.dto.AuthResponseStatusesEnum;
import yf.user.dto.LoginDTO;
import yf.user.dto.UserAllDataDto;
import yf.user.dto.UserDto;
import yf.user.dto.external.FBUserDTO;
import yf.user.dto.external.VKUserDTO;
import yf.user.entities.User;
import yf.user.entities.Verifications;
import yf.user.rest.VkRestClient;

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

    public UserAllDataDto getUserById(final Long userId) {
        return userWorkflow.getUserById(userId);
    }

    public UserDto getBasicUserById(final Long userId) {
        return userWorkflow.getBasicUserById(userId);
    }

    public Response getVkUser(final Long socialId) {
        UserAllDataDto dto = userWorkflow.getUserByVkSocialId(socialId);
        if (dto == null) {
            return Response.status(401)
                    .entity(AuthResponseStatusesEnum.NOT_EXIST)
                    .build();
        }

        if (dto.getUser()
                .getAuthorized() == null
                || !dto.getUser()
                        .getAuthorized()) {
            return Response.status(401)
                    .entity(AuthResponseStatusesEnum.NOT_AUTHORIZED)
                    .entity(dto)
                    .build();
        }

        return Response.ok()
                .entity(dto)
                .build();

    }

    public UserAllDataDto getFbUser(final Long socialId) {
        return userWorkflow.getUserByFbSocialId(socialId);

    }

    public VKUserDTO createVKUser(final long userId, final LoginDTO loginDTO) {
        final User user  = registerUser(loginDTO);
        /// TODO
        final VKUserDTO vKUserDTO = userRestClient.getVKUserDetails(userId);
        return userWorkflow.saveAndVerifyVKUser(vKUserDTO, user);
    }

    public FBUserDTO createFBUser(final FBUserDTO fbUserDTO) {
        return userWorkflow.saveFBUser(fbUserDTO);
    }

    public Response getUserByEmailNickNameAndPassword(final LoginDTO loginDTO) {
        final User user = userWorkflow.getUserByEmailNickName(loginDTO.getUser());
        if (user == null) {
            return Response.status(401).entity(AuthResponseStatusesEnum.NOT_EXIST).build();
        }
//        if (!user.isAuthorize()) {
//            return Response.status(401).entity(AuthResponseStatusesEnum.NOT_AUTHORIZED).build();
//        }
        if (!user.getPassword().equals(loginDTO.getPassword())) {
            return Response.status(401).entity(AuthResponseStatusesEnum.WRONG_PASSWORD).build();
        }

        final UserAllDataDto userSocialAccounts = userWorkflow.getUserSocialAccounts(user);

        Map<String, Object> resp = new HashMap<>();
        resp.put("user", userSocialAccounts);
        resp.put("token", jwtService.createToken(userSocialAccounts.getUser()));

        return Response.status(200).entity(resp).build();
    }

    public User registerUser(final LoginDTO loginDTO) {
        final User userToRegister = userWorkflow.registerUser(loginDTO);
        emailService.sendVerificationEmail(userToRegister);
        return userToRegister;
    }

    public User getUserByEmailNickName(final LoginDTO loginDTO) {
        return userWorkflow.getUserByEmailNickName(loginDTO.getUser());
    }

    public AuthResponseStatusesEnum newUserValidityCheck(final User user,
                                                          final LoginDTO loginDTO) {
        if (user != null) {
            return AuthResponseStatusesEnum.USER_ALREADY_EXIST;
        }

        if (loginDTO.getPassword()
                .length() < 7 || loginDTO.getPassword()
                        .matches("\\s+")) {
            return AuthResponseStatusesEnum.PASSWORD_NOT_VALID;
        }
        // no error => is ok
        return null;

    };

    public Response validateToken(final Long userId,
                                  final String token) {
        final Boolean isValid = jwtService.isValidToken(token,
                userId);
        return Response.ok()
                .entity(isValid)
                .build();
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

}
