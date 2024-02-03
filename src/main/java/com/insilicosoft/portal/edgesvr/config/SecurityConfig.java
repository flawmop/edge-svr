package com.insilicosoft.portal.edgesvr.config;

import reactor.core.publisher.Mono;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.security.web.server.csrf.XorServerCsrfTokenRequestAttributeHandler;
import org.springframework.web.server.WebFilter;

@Configuration
public class SecurityConfig {

  @Bean
  SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
    return http.authorizeExchange(exchange -> exchange.pathMatchers("/", "/css/*", "/js/*", "/icon/*", "/img/*").permitAll()
                                                      .anyExchange().authenticated())
                                 .csrf(csrf -> csrf.csrfTokenRepository(CookieServerCsrfTokenRepository.withHttpOnlyFalse())
                                                   .csrfTokenRequestHandler(new XorServerCsrfTokenRequestAttributeHandler()::handle))
                                 .oauth2Login(Customizer.withDefaults())
                                 .build();
    }

  @Bean
  WebFilter csrfWebFilter() {
    // Required because of https://github.com/spring-projects/spring-security/issues/5766
    return (exchange, chain) -> {
      exchange.getResponse()
              .beforeCommit(() -> Mono.defer(() -> {
                                                     Mono<CsrfToken> csrfToken = exchange.getAttribute(CsrfToken.class.getName());
                                                     return csrfToken != null ? csrfToken.then() : Mono.empty();
                                                   }));
      return chain.filter(exchange);
    };
  }

}