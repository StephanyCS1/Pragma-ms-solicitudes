package co.com.crediya.api.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.jwt")
public record JwtProps(String issuer, String audience, String secret) {}