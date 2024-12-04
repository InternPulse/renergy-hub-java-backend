package RenergyCartService.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.SignatureException;
import java.util.Date;

@Component
public class JwtTokenUtil {

    @Autowired
    private JwtConfig jwtConfig;

    public Claims validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(jwtConfig.getSecretKey().getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid or expired token");
        }
    }

    public String extractUsername(Claims claims) {
        return claims.getSubject();
    }

    public Long extractUserId(Claims claims) {
        return claims.get("userId", Long.class);
    }

    public String[] extractRoles(Claims claims) {
        return claims.get("roles", String.class).split(",");
    }
}