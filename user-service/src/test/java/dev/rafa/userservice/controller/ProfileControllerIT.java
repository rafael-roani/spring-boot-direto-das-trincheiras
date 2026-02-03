package dev.rafa.userservice.controller;

import dev.rafa.userservice.commons.FileUtils;
import dev.rafa.userservice.commons.ProfileUtils;
import dev.rafa.userservice.response.ProfileGetResponse;
import dev.rafa.userservice.response.ProfilePostResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProfileControllerIT {

    private static final String URL = "/v1/profiles";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ProfileUtils profileUtils;

    @Autowired
    private FileUtils fileUtils;

    @Test
    @Order(1)
    @Sql(value = "/sql/init_two_profiles.sql")
    @DisplayName("GET v1/profiles returns a list with all profiles")
    void findAll_ReturnsAllProfiles_WhenSuccessful() throws Exception {
        ParameterizedTypeReference<List<ProfileGetResponse>> typeReference = new ParameterizedTypeReference<>() {};

        ResponseEntity<List<ProfileGetResponse>> response = testRestTemplate.exchange(URL, GET, null, typeReference);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull().doesNotContainNull();

        response.getBody().forEach(r -> Assertions.assertThat(r).hasNoNullFieldsOrProperties());
    }

    @Test
    @Order(2)
    @DisplayName("GET v1/profiles returns empty list when nothing is not found")
    void findAll_ReturnsEmptyList_WhenNothingIsNotFound() throws Exception {
        ParameterizedTypeReference<List<ProfileGetResponse>> typeReference = new ParameterizedTypeReference<>() {};

        ResponseEntity<List<ProfileGetResponse>> response = testRestTemplate.exchange(URL, GET, null, typeReference);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull().isEmpty();
    }

    @Test
    @Order(3)
    @DisplayName("POST v1/profiles creates a profile")
    void save_CreatesProfile_WhenSuccessful() throws Exception {
        String requestJson = fileUtils.readResourceFile("profile/post-request-profile-200.json");

        HttpEntity<String> profileEntity = buildHttpEntity(requestJson);

        ResponseEntity<ProfilePostResponse> response = testRestTemplate.exchange(URL, POST, profileEntity, ProfilePostResponse.class);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(response.getBody()).isNotNull().hasNoNullFieldsOrProperties();
    }

    private static HttpEntity<String> buildHttpEntity(String body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, httpHeaders);
    }

}
