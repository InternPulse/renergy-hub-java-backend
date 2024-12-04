package RenergyCartService.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TokenValidationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;

    public TokenValidationFilter(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                var claims = jwtTokenUtil.validateToken(token);
                String username = jwtTokenUtil.extractUsername(claims);
                Long userId = jwtTokenUtil.extractUserId(claims);
                String[] roles = jwtTokenUtil.extractRoles(claims);

                // Create authentication token and set it in SecurityContext
                var authToken = new UsernamePasswordAuthenticationToken(
                        username, null, AuthorityUtils.createAuthorityList(roles));
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

                // Optionally, set userId in the request (e.g., for downstream services)
                request.setAttribute("userId", userId);
            } catch (IllegalArgumentException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(e.getMessage());
                return;
            }
        }

        chain.doFilter(request, response);
    }
}