package co.com.crediya.api.security;

import co.com.crediya.api.dto.JwtProps;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class JwtService {
    private final JwtProps props;
    private Key key;

    public JwtService(JwtProps props) { this.props = props; }

    @PostConstruct
    void init() { this.key = Keys.hmacShaKeyFor(props.secret().getBytes(StandardCharsets.UTF_8)); }

    public Jws<Claims> parseAndValidate(String jwt) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .requireIssuer(props.issuer())
                .requireAudience(props.audience())
                .build()
                .parseClaimsJws(jwt);
    }
}