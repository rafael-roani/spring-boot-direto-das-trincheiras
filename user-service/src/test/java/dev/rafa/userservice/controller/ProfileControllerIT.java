package dev.rafa.userservice.controller;

import dev.rafa.userservice.response.ProfileGetResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProfileControllerIT {

    private static final String URL = "/v1/profiles";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @Order(1)
    @Sql(value = "/sql/init_two_profiles.sql")
    @DisplayName("GET v1/profiles returns a list with all profiles")
    void findAll_ReturnsAllProfiles_WhenSuccessful() throws Exception {
        ParameterizedTypeReference<List<ProfileGetResponse>> typeReference = new ParameterizedTypeReference<>() {};

        ResponseEntity<List<ProfileGetResponse>> response = testRestTemplate.exchange(URL, HttpMethod.GET, null, typeReference);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull().doesNotContainNull();

        response.getBody().forEach(r -> Assertions.assertThat(r).hasNoNullFieldsOrProperties());
    }

}
