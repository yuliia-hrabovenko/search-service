package net.guzari.search.rest.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import net.guzari.search.rest.exceptions.CustomException;
import net.guzari.search.rest.exceptions.ExceptionUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    public static final int BEGIN_INDEX = 7;
    public static final String BEARER = "Bearer";
    @Value("${jwt.secret}")
    private String jwtSigningKey;

    public String validateHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            throw new CustomException(ExceptionUtil.AUTH_HEADER_EXCEPTION);
        }
        return authHeader.substring(BEGIN_INDEX);
    }

    public Claims validateToken(String token) throws CustomException {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(jwtSigningKey).parseClaimsJws(token).getBody();
        } catch (JwtException | IllegalArgumentException e) {
            throw new CustomException(ExceptionUtil.INVALID_TOKEN_EXCEPTION);
        }

        if (claims.getExpiration().before(new Date())) {
            throw new CustomException(ExceptionUtil.EXPIRED_TOKEN_EXCEPTION);
        }
        return claims;
    }

}
