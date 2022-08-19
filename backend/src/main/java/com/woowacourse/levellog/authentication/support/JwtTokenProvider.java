package com.woowacourse.levellog.authentication.support;

import com.woowacourse.levellog.authentication.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final long validityInMilliseconds;

    public JwtTokenProvider(@Value("${security.jwt.token.secret-key}") final String keyString,
                            @Value("${security.jwt.token.expire-length}") final long validityInMilliseconds) {
        this.secretKey = Keys.hmacShaKeyFor(keyString.getBytes(StandardCharsets.UTF_8));
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String createToken(final String payload) {
        final Date now = new Date();
        final Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(payload)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getPayload(final String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (final JwtException e) {
            throw new InvalidTokenException("token에서 payload 추출 실패 - token:" + token);
        }
    }

    public boolean validateToken(final String token) {
        try {
            final Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);

            Long.parseLong(claims.getBody().getSubject());

            return !claims.getBody()
                    .getExpiration()
                    .before(new Date());
        } catch (final JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public boolean validateAdminToken(final String token) {
        try {
            final Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);

            final String subject = claims.getBody().getSubject();
            if (!subject.equals("This is admin token.")) {
                throw new InvalidTokenException("관리자 토큰이 아닙니다.");
            }

            return !claims.getBody()
                    .getExpiration()
                    .before(new Date());
        } catch (final JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
