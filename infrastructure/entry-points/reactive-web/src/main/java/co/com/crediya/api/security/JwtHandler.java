package co.com.crediya.api.security;

import co.com.crediya.api.dto.NewRequest;
import co.com.crediya.api.dto.RequestDto;
import co.com.crediya.model.solicitud.exceptions.DomainValidationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class JwtHandler {

    @Value("${jwt.secret}")
    private String jwtSecret;

    public NewRequest toNewRequest(RequestDto request, String token) {
        Claims claims = parseToken(token);

        String email = claims.get("email", String.class);
        String identification = claims.get("identification", String.class);
        UUID userId = UUID.fromString(claims.get("uid", String.class));
        String name = claims.get("name", String.class);
        String lastName = claims.get("lastName", String.class);

        return new NewRequest(
                identification, name, lastName, email,
                request.phoneNumber(), request.requestedAmount(),
                request.loanTermMonths(), request.loanTypeId(),
                request.monthlyIncome(), userId
        );
    }

    private Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Mono<String> validateToken(ServerHttpRequest request) {
        var auth = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (auth == null || !auth.startsWith("Bearer ")) {
            return Mono.error(new DomainValidationException("Falta token de autorización"));
        }
        String bearer = auth.substring(7);
        if (bearer.isBlank()) {
            return Mono.error(new DomainValidationException("Token vacío"));
        }
        return Mono.just(bearer);
    }
}