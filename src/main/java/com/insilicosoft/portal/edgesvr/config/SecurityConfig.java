package com.insilicosoft.portal.edgesvr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
  SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
    return http.authorizeExchange(exchange -> exchange.anyExchange().authenticated())
                                 .oauth2Login(Customizer.withDefaults())
                                 .build();
    }

}
