package com.example.api.v1.hello;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.tomakehurst.wiremock.common.Slf4jNotifier;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

@MicronautTest
class HelloControllerTest {

  private static final String HELLO_URI = "/api/v1/user/1/hello";

  @SuppressWarnings("unused")
  @RegisterExtension
  private static final WireMockExtension WIRE_MOCK =
      WireMockExtension.newInstance()
          .options(wireMockConfig().port(9090).notifier(new Slf4jNotifier(false)))
          .configureStaticDsl(true)
          .build();

  @Inject @Client("/") private HttpClient client;

  @Test
  void shouldReturnHelloNameCorrectly() {
    stubFor(
        get(urlPathEqualTo("/some-service/user/1/name"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN)
                    .withBody("John")));

    HttpResponse<String> response = client.toBlocking().exchange(HttpRequest.GET(HELLO_URI), String.class);
    assertThat(response.body()).isEqualTo("Hello John!");

    // perform a second call to demonstrate the issue with MdcInstrumenter logging
    client.toBlocking().exchange(HttpRequest.GET(HELLO_URI), String.class);
  }
}
