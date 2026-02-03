package support;

import config.KeyProperties;
import constants.PassportConstants;
import exception.InvalidPassportException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import model.PassportClaims;

import javax.crypto.SecretKey;
import java.util.Arrays;

public class PassportVerifier {

    private final SecretKey key;

    public PassportVerifier(KeyProperties keyProperties) {
        this.key = keyProperties.toSecretKey();
    }

    public PassportClaims verify(String encodedPassport) {
        try {
            Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(encodedPassport)
                .getPayload();

            return new PassportClaims(
                claims.get(PassportConstants.PASSPORT_USER_ID).toString(),
                Arrays.asList(claims.get(PassportConstants.PASSPORT_ROLE).toString().split(",")),
                claims.get(PassportConstants.PASSPORT_TRACE_ID).toString(),
                encodedPassport
            );

        } catch (ExpiredJwtException e) {
            throw new InvalidPassportException("Expired passport");

        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidPassportException("Invalid passport");
        }
    }
}
