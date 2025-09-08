package co.com.crediya.api.config;

import co.com.crediya.api.security.JwtService;
import io.jsonwebtoken.Claims;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, JwtService jwtService) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .authorizeExchange(reg -> reg
                        // Swagger (springdoc)
                        .pathMatchers("/swagger-ui.html","/swagger-ui/**",
                                "/v3/api-docs","/v3/api-docs/**",
                                "/v3/api-docs/swagger-config","/webjars/**").permitAll()
                        // protege el micro:
                        .pathMatchers(HttpMethod.POST, "/api/v1/solicitud/**")
                        .hasAnyRole("USER","ASESOR","ADMIN")
                        .pathMatchers("/api/v1/solicitud/**")
                        .hasAnyRole("USER","ASESOR","ADMIN")
                        .anyExchange().authenticated()
                )
                .addFilterAt((exchange, chain) -> bearer(exchange)
                                .flatMap(token -> authenticate(token, jwtService))
                                .flatMap(auth -> chain.filter(exchange)
                                        .contextWrite(ctx -> org.springframework.security.core.context
                                                .ReactiveSecurityContextHolder.withAuthentication(auth)))
                                .switchIfEmpty(chain.filter(exchange)),
                        SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }


    private Mono<String> bearer(ServerWebExchange exchange) {
        var h = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        return (h != null && h.startsWith("Bearer ")) ? Mono.just(h.substring(7)) : Mono.empty();
    }

    private Mono<AbstractAuthenticationToken> authenticate(String token, JwtService jwtService) {
        try {
            var jws = jwtService.parseAndValidate(token);
            Claims claims = jws.getBody();

            String rol = claims.get("rol", String.class);
            var authorities = (rol == null)
                    ? List.<GrantedAuthority>of()
                    : List.of(new SimpleGrantedAuthority("ROLE_" + rol));

            var details = Map.copyOf(claims);
            var auth = new AbstractAuthenticationToken((Collection<? extends GrantedAuthority>) authorities) {
                @Override public Object getCredentials() { return token; }
                @Override public Object getPrincipal()   { return claims.get("email"); }
            };
            auth.setAuthenticated(true);
            auth.setDetails(details);
            return Mono.just(auth);
        } catch (Exception e) {
            return Mono.error(new BadCredentialsException("Invalid JWT", e));
        }
    }
}