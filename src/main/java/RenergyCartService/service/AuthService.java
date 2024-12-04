package RenergyCartService.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.SignatureException;
import java.util.Collections;

@Service
public class AuthService {

    private final String jwtSecret = System.getenv("JWT_SECRET");

    public boolean isValidToken(String token) {
        try {
            // Parse the token using the secret
            Jwts.parserBuilder()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // Log the exception for debugging
            return false;
        }
    }

    public UserDetails getUserDetails(String token) {
        try {
            // Parse the token and extract claims
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject(); // The subject of the token
            String roles = claims.get("roles", String.class); // Custom claim for roles

            // Return a UserDetails object
            return new User(username, "", Collections.emptyList()); // Replace emptyList() with authorities if needed
        } catch (Exception e) {
            // Handle token parsing exceptions
            return null;
        }
    }
}