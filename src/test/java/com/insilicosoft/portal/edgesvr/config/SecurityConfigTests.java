package com.insilicosoft.portal.edgesvr.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest
@Import(SecurityConfig.class)
class SecurityConfigTests {

  @Autowired
  WebTestClient webClient;

  @MockBean
  ReactiveClientRegistrationRepository mockReactiveClientRegistrationRepository;

  @Test
  void whenNotLoggedInAndAccessingUnsecuredButDoesNotExistThen404() {
    webClient.get().uri("/img/doesNotExist.png").exchange().expectStatus().isNotFound();
  }

  @Test
  void whenNotLoggedInAndAccessingNonPermitAllThen302() {
    webClient.get().uri("/nonPermitAll.html").exchange().expectStatus().isFound();
  }

}