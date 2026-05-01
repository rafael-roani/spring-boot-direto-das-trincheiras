package dev.rafa.userservice.controller;

import dev.rafa.userservice.commons.FileUtils;
import dev.rafa.userservice.config.IntegrationsTestConfig;
import dev.rafa.userservice.config.RestAssuredConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RestAssuredConfig.class)
@Sql(value = "/sql/user/init_one_login_regular_user.sql")
@Sql(value = "/sql/user/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@AutoConfigureWireMock(port = 0, files = "classpath:/wiremock/brasil-api/cep", stubs = "classpath:/wiremock/brasil-api/cep/mappings")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BrasilApiControllerIT extends IntegrationsTestConfig {

    private static final String URL = "v1/brasil-api/cep";

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    @Qualifier(value = "requestSpecificationRegularUser")
    private RequestSpecification requestSpecificationRegularUser;

    @BeforeEach
    void setUrl() {
        RestAssured.requestSpecification = requestSpecificationRegularUser;
    }

    @Order(1)
    @Test
    @DisplayName("findCep returns a CepGetResponse when successful")
    public void findCep_ReturnsCepGetResponse_WhenSuccessful() {
        String cep = "12345678";
        String expectedResponse = fileUtils.readResourceFile("brasil-api/cep/expected-get-cep-response-200.json");

        String actualResponse = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .get(URL + "/{cep}", cep)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .asString();

        assertThatJson(actualResponse).isEqualTo(expectedResponse);
    }

    @Order(1)
    @Test
    @DisplayName("findCep returns a CepErrorResponse when unsuccessful")
    public void findCep_ReturnsCepErrorResponse_WhenUnsuccessful() {
        String cep = "43215678";
        String expectedResponse = fileUtils.readResourceFile("brasil-api/cep/expected-get-cep-response-404.json");

        String actualResponse = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .get(URL + "/{cep}", cep)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .asString();

        assertThatJson(actualResponse).isEqualTo(expectedResponse);
    }

}
