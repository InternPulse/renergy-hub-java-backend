package RenergyCartService.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.token.header}")
    private String tokenHeader;

    @Value("${jwt.token.prefix}")
    private String tokenPrefix;

    @Value("${jwt.token.expiration.time}")
    private long tokenExpirationTime;
}

