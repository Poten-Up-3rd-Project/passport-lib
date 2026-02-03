package support;

import config.KeyProperties;
import constants.PassportConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import model.PassportClaims;

import javax.crypto.SecretKey;
import java.util.Date;

public class PassportEncoder {

    private final SecretKey key;
    private final int duration;

    public PassportEncoder(KeyProperties properties) {
        this.key = properties.toSecretKey();
        this.duration = properties.getDurationMillis();
    }

    /**
     * Passport 객체를 JWT 문자열로 변환합니다.
     * <p>
     * 클레임 명세: PASSPORT_CLAIM_SPECIFICATION.md 참고
     * - uid: 사용자 ID
     * - rol: 역할 (쉼표로 구분된 문자열)
     * - tid: 분산 추적 ID
     *
     * @param passport Passport 객체
     * @return 서명된 Passport JWT 문자열
     */
    public String encode(PassportClaims passport) {
        // 역할을 쉼표로 구분된 문자열로 변환
        String rolesString = String.join(PassportConstants.ROLE_SEPARATOR, passport.roles());

        Claims claims = Jwts.claims()
            .add(PassportConstants.PASSPORT_USER_ID, passport.userId())
            .add(PassportConstants.PASSPORT_ROLE, rolesString)
            .add(PassportConstants.PASSPORT_TRACE_ID, passport.traceId())
            .build();

        return Jwts.builder()
            .claims(claims)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + duration))
            .signWith(key)
            .compact();
    }
}
