package dev.rafa.userservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.rafa.commonscore.exception.NotFoundException;
import dev.rafa.userservice.commons.CepUtils;
import dev.rafa.userservice.config.BrasilApiConfigurationProperties;
import dev.rafa.userservice.config.RestClientConfiguration;
import dev.rafa.userservice.response.CepErrorResponse;
import dev.rafa.userservice.response.CepGetResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.RequestMatcher;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.DefaultResponseCreator;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestClient;

@RestClientTest({
    BrasilApiService.class,
    RestClientConfiguration.class,
    BrasilApiConfigurationProperties.class,
    ObjectMapper.class,
    CepUtils.class
})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BrasilApiServiceTest {

  @Autowired
  private BrasilApiService service;

  @Autowired
  @Qualifier("brasilApiClient")
  private RestClient.Builder brasilApiClient;

  @Autowired
  private MockRestServiceServer server;

  @Autowired
  private BrasilApiConfigurationProperties properties;

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private CepUtils cepUtils;

  @AfterEach
  void reset() {
    server.reset();
  }

  @Order(1)
  @Test
  @DisplayName("findCep returns a CepGetResponse when successful")
  public void findCep_ReturnsCepGetResponse_WhenSuccessful() throws JsonProcessingException {
    server = MockRestServiceServer.bindTo(brasilApiClient).build();

    String cep = "12345678";
    CepGetResponse cepGetResponse = cepUtils.newCepGetResponse();
    String jsonResponse = mapper.writeValueAsString(cepGetResponse);

    RequestMatcher requestTo = MockRestRequestMatchers.requestToUriTemplate(properties.baseUrl() + properties.cepUri(), cep);
    DefaultResponseCreator withSuccess = MockRestResponseCreators.withSuccess(jsonResponse, MediaType.APPLICATION_JSON);

    server.expect(requestTo).andRespond(withSuccess);

    Assertions.assertThat(service.findCep(cep))
        .isNotNull()
        .isEqualTo(cepGetResponse);
  }

  @Order(1)
  @Test
  @DisplayName("findCep returns a CepErrorResponse when unsuccessful")
  public void findCep_ReturnsCepErrorResponse_WhenUnsuccessful() throws JsonProcessingException {
    server = MockRestServiceServer.bindTo(brasilApiClient).build();

    String cep = "43215678";
    CepErrorResponse cepErrorResponse = cepUtils.newCepErrorResponse();
    String jsonResponse = mapper.writeValueAsString(cepErrorResponse);
    String expectedErrorMessage = "404 NOT_FOUND \"CepErrorResponse[name=CepPromiseError, message=Todos os serviços de CEP retornaram erro., type=service_error, errors=[CepInnerErrorResponse[name=ServiceError, message=CEP INVÁLIDO, service=correios]]]\"";

    RequestMatcher requestTo = MockRestRequestMatchers.requestToUriTemplate(properties.baseUrl() + properties.cepUri(), cep);
    DefaultResponseCreator withSuccess = MockRestResponseCreators.withResourceNotFound()
        .body(jsonResponse)
        .contentType(MediaType.APPLICATION_JSON);

    server.expect(requestTo).andRespond(withSuccess);

    Assertions.assertThatException()
        .isThrownBy(() -> service.findCep(cep))
        .withMessage(expectedErrorMessage)
        .isInstanceOf(NotFoundException.class);
  }

}