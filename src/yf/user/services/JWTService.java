package yf.user.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import yf.user.dto.ProfilePictureDTO;
import yf.user.dto.UserDto;

import java.util.Optional;
import java.util.logging.Logger;

public class JWTService {

    private static final Logger LOG = Logger.getLogger(JWTService.class.getName());

    private static final String SECRETE = "yfsecret";
    private static final String ISSUSER = "youngfolks.ru";

    public String createToken(final UserDto userDto) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRETE);
            return JWT.create()
                    .withClaim("id",
                            userDto.getId())
                    .withClaim("email",
                            userDto.getEmail())
                    .withClaim("firstName",
                            userDto.getFirstName())
                    .withClaim("lastName",
                            userDto.getLastName())
                    .withClaim("nickName",
                            userDto.getNickName())
                    .withClaim("avatar",
                            Optional.ofNullable(userDto.getProfilePictureDTO())
                                    .map(ProfilePictureDTO::getFileId)
                                    .orElse(null))
                    .withIssuer(ISSUSER)
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            LOG.severe("JWT Exception" + e);
        }
        return null;
    }

    public boolean isValidToken(final String token,
                                final Long userId) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRETE);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUSER)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaim("id")
                    .asLong()
                    .equals(userId)
                    && jwt.getIssuer()
                            .equals(ISSUSER);
        } catch (JWTVerificationException e) {
            LOG.severe("JWT Exception" + e);
        }
        return false;
    }
}
