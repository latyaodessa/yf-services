package yf.user.services;

import yf.core.JSONService;
import yf.mail.services.EmailService;
import yf.user.UserGetDao;
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

import javax.inject.Inject;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

public class UserService {
    @Inject
    private UserWorkflow userWorkflow;
    @Inject
    private VkRestClient userRestClient;
    @Inject
    private EmailService emailService;
    @Inject
    private JSONService jsonService;
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

    public UserAllDataDto getVkUser(final Long socialId) {
        return userWorkflow.getUserByVkSocialId(socialId);

    }

    public UserAllDataDto getFbUser(final Long socialId) {
        return userWorkflow.getUserByFbSocialId(socialId);

    }

    public VKUserDTO createVKUser(final long userId) {
        VKUserDTO vKUserDTO = userRestClient.getVKUserDetails(userId);
        return userWorkflow.saveAndVerifyVKUser(vKUserDTO);
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

        return Response.ok()
                .cookie(new NewCookie(new Cookie("user", jsonService.objectToJSON(userSocialAccounts)), "user", 86400, true))
                .cookie(new NewCookie(new Cookie("token", jwtService.createToken(userSocialAccounts.getUser())), "token", 86400, true))
                .build();
    }

    public Response registerUser(final LoginDTO loginDTO) {
        final User user = userWorkflow.getUserByEmailNickName(loginDTO.getUser());
        if (user != null) {
            return Response.status(401).entity(AuthResponseStatusesEnum.USER_ALREADY_EXIST).build();
        }

        final User userToRegister = userWorkflow.registerUser(loginDTO);
        emailService.sendVerificationEmail(userToRegister);

        final UserAllDataDto userSocialAccounts = userWorkflow.getUserSocialAccounts(userToRegister);


        return Response.ok()
                .cookie(new NewCookie(new Cookie("user", jsonService.objectToJSON(userSocialAccounts)), "user", 86400, true))
                .cookie(new NewCookie(new Cookie("token", jwtService.createToken(userSocialAccounts.getUser())), "token", 86400, true))
                .build();
    }

    public Response validateToken(final Long userId, final String token) {
        final Boolean isValid = jwtService.isValidToken(token, userId);
        return Response.ok().entity(isValid).build();
    }


    public Response requestResetPassword(final String email) {
        final User user = userWorkflow.getUserByEmailNickName(email);
        if (user == null) {
            return Response.status(401).entity(AuthResponseStatusesEnum.NOT_EXIST).build();
        }

        emailService.sendResetPasswordEmail(user);

        return Response.ok().build();
    }


    public Response resetPassword(final String verification, final String newPassword, final String repeatPassword) {

        if (!newPassword.equals(repeatPassword)) {
            return Response.status(401).entity(AuthResponseStatusesEnum.PASSWORDS_NOT_MATCING).build();

        }

        final Verifications verifications = verificationService.validateVerification(verification);

        if (verifications == null) {
            return Response.status(401).entity(AuthResponseStatusesEnum.VERIFICATION_NOT_VALID).build();
        }


        final User user = userWorkflow.changeUserPassword(verifications.getUserId(), newPassword);

        if (user == null) {
            return Response.status(401).entity(AuthResponseStatusesEnum.NOT_EXIST).build();
        }

        return Response.ok().build();
    }

    public Response verifyUserVerification(final String verification) {
        final Verifications ver = verificationService.validateVerification(verification);
        if (verification == null) {
            return Response.status(401).entity(AuthResponseStatusesEnum.VERIFICATION_NOT_VALID).build();
        }

        userWorkflow.verifyUserVerification(ver.getUserId(), ver.getType());

        return Response.ok().build();
    }


}
