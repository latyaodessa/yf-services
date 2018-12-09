package yf.user.rest;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import yf.user.UserDao;
import yf.user.dto.AuthResponseStatusesEnum;
import yf.user.dto.LoginDTO;
import yf.user.dto.UserAllDataDto;
import yf.user.entities.User;
import yf.user.services.JWTService;

public class AuthRestHelper {

    @Inject
    private JWTService jwtService;
    @Inject
    private UserDao userDao;

    public Response isRegistrationValid(final LoginDTO loginDTO) {

        final User user = userDao.getUserByNicknameOrEmail(loginDTO.getUser());

        AuthResponseStatusesEnum error = newUserValidityCheck(user,
                loginDTO);

        if (error != null) {
            return Response.status(401)
                    .entity(error)
                    .build();
        }

        return null;
    }

    public Response isAuthValid(final LoginDTO loginDTO,
                                final User user) {

        AuthResponseStatusesEnum error = loginUserValidityCheck(user,
                loginDTO);

        if (error != null) {
            return Response.status(401)
                    .entity(error)
                    .build();
        }

        return null;
    }

    public Response isSocialAuthValid(final UserAllDataDto dataDto) {

        AuthResponseStatusesEnum error = socialLoginValidityCheck(dataDto);

        if (error != null) {
            return Response.status(401)
                    .entity(error)
                    .build();
        }

        return null;
    }

    public Map<String, Object> userAuthResponseEntityMap(final UserAllDataDto dto) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("user",
                dto);
        resp.put("token",
                jwtService.createToken(dto.getUser()));
        return resp;
    }

    private AuthResponseStatusesEnum newUserValidityCheck(final User user,
                                                          final LoginDTO loginDTO) {
        if (user != null) {
            return AuthResponseStatusesEnum.USER_ALREADY_EXIST;
        }

        AuthResponseStatusesEnum passwordError = passwordValidityCheck(loginDTO);

        if (passwordError != null) {
            return passwordError;
        }
        // no error => is ok
        return null;

    }

    private AuthResponseStatusesEnum passwordValidityCheck(final LoginDTO loginDTO) {
        if (loginDTO.getPassword()
                .length() < 7 || loginDTO.getPassword()
                        .matches("\\s+")) {
            return AuthResponseStatusesEnum.PASSWORD_NOT_VALID;
        }

        // no error => is ok
        return null;

    }

    private AuthResponseStatusesEnum loginUserValidityCheck(final User user,
                                                            final LoginDTO loginDTO) {
        if (user == null) {
            return AuthResponseStatusesEnum.NOT_EXIST;
        }
        // if (!user.isAuthorize()) {
        // return Response.status(401).entity(AuthResponseStatusesEnum.NOT_AUTHORIZED).build();
        // }
        if (!user.getPassword()
                .equals(loginDTO.getPassword())) {
            return AuthResponseStatusesEnum.WRONG_PASSWORD;
        }

        // no error => is ok
        return null;

    }

    private AuthResponseStatusesEnum socialLoginValidityCheck(final UserAllDataDto dto) {

        if (dto == null) {
            return AuthResponseStatusesEnum.NOT_EXIST;
        }

        if (dto.getUser()
                .getAuthorized() == null
                || !dto.getUser()
                        .getAuthorized()) {
            return AuthResponseStatusesEnum.NOT_AUTHORIZED;
        }

        // no error => is ok
        return null;

    }
}
