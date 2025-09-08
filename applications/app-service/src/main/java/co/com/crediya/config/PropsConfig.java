package co.com.crediya.config;

import co.com.crediya.api.dto.JwtProps;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(JwtProps.class)
class PropsConfig {}