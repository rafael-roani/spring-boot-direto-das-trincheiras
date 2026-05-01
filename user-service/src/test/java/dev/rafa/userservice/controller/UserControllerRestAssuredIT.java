package dev.rafa.userservice.controller;

import dev.rafa.commonscore.exception.NotFoundException;
import dev.rafa.userservice.commons.FileUtils;
import dev.rafa.userservice.commons.UserUtils;
import dev.rafa.userservice.config.IntegrationsTestConfig;
import dev.rafa.userservice.config.RestAssuredConfig;
import dev.rafa.userservice.domain.User;
import dev.rafa.userservice.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import java.util.List;
import java.util.stream.Stream;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import net.javacrumbs.jsonunit.core.Option;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RestAssuredConfig.class)
class UserControllerRestAssuredIT extends IntegrationsTestConfig {

  private static final String URL = "/v1/users";

  @Autowired
  private UserUtils userUtils;

  @Autowired
  private FileUtils fileUtils;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UserRepository repository;

  @Autowired
  @Qualifier(value = "requestSpecificationRegularUser")
  private RequestSpecification requestSpecificationRegularUser;

  @Autowired
  @Qualifier(value = "requestSpecificationAdminUser")
  private RequestSpecification requestSpecificationAdminUser;

  @BeforeEach
  void setUrl() {
    RestAssured.requestSpecification = requestSpecificationRegularUser;
  }

  @Test
  @Order(1)
  @Sql(value = "/sql/user/init_three_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(value = "/sql/user/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("GET v1/users returns a list with all users when argument is null")
  void findAll_ReturnsAllUsers_WhenArgumentIsNull() {
    RestAssured.requestSpecification = requestSpecificationAdminUser;

    String expectedResponse = fileUtils.readResourceFile("user/get-user-null-name-200.json");

    String response = RestAssured.given()
        .contentType(ContentType.JSON).accept(ContentType.JSON)
        .when()
        .get(URL)
        .then()
        .statusCode(HttpStatus.OK.value())
        .log().all()
        .extract().response().body().asString();

    JsonAssertions.assertThatJson(response)
        .inPath("$[*].id")
        .isArray()
        .allSatisfy(node -> {
          Assertions.assertThat(node).isInstanceOf(Number.class);
          Assertions.assertThat(((Number) node).longValue()).isPositive();
        });

    JsonAssertions.assertThatJson(response)
        .whenIgnoringPaths("$[*].id")
        .when(Option.IGNORING_ARRAY_ORDER)
        .isEqualTo(expectedResponse);
  }

  @Test
  @Order(2)
  @Sql(value = "/sql/user/init_three_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(value = "/sql/user/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("GET v1/users?name=Pedro returns a list with found object when name exists")
  void findAll_ReturnsFoundUserList_WhenNameIsFound() {
    RestAssured.requestSpecification = requestSpecificationAdminUser;

    String expectedResponse = fileUtils.readResourceFile("user/get-user-pedro-name-200.json");
    String name = "Pedro";

    String response = RestAssured.given()
        .contentType(ContentType.JSON).accept(ContentType.JSON)
        .param("name", name)
        .when()
        .get(URL)
        .then()
        .statusCode(HttpStatus.OK.value())
        .log().all()
        .extract().response().body().asString();

    JsonAssertions.assertThatJson(response)
        .inPath("$[*].id")
        .isArray()
        .allSatisfy(node -> {
          Assertions.assertThat(node).isInstanceOf(Number.class);
          Assertions.assertThat(((Number) node).longValue()).isPositive();
        });

    JsonAssertions.assertThatJson(response)
        .whenIgnoringPaths("$[*].id")
        .when(Option.IGNORING_ARRAY_ORDER)
        .isEqualTo(expectedResponse);

    JsonAssertions.assertThatJson(response)
        .inPath("$[0].firstName")
        .isEqualTo(name);
  }

  @Test
  @Order(3)
  @Sql(value = "/sql/user/init_three_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(value = "/sql/user/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("GET v1/users?name=x returns empty list when name is not found")
  void findAll_ReturnsEmptyList_WhenNameIsNotFound() {
    RestAssured.requestSpecification = requestSpecificationAdminUser;

    String name = "x";

    String response = RestAssured.given()
        .contentType(ContentType.JSON).accept(ContentType.JSON)
        .param("name", name)
        .when()
        .get(URL)
        .then()
        .statusCode(HttpStatus.OK.value())
        .log().all()
        .extract().response().body().asString();

    JsonAssertions.assertThatJson(response)
        .isArray()
        .isEmpty();
  }

  @Test
  @Order(4)
  @Sql(value = "/sql/user/init_three_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(value = "/sql/user/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("GET v1/users/1 returns an object with given id")
  void findById_ReturnsUserById_WhenSuccessful() {
    String expectedResponse = fileUtils.readResourceFile("user/get-user-by-id-200.json");
    List<User> users = repository.findByFirstNameIgnoreCase("Carlos");

    Assertions.assertThat(users).isNotEmpty().hasSize(1);

    String response = RestAssured.given()
        .contentType(ContentType.JSON).accept(ContentType.JSON)
        .pathParam("id", users.getFirst().getId())
        .when()
        .get(URL + "/{id}")
        .then()
        .statusCode(HttpStatus.OK.value())
        .log().all()
        .extract().response().body().asString();

    JsonAssertions.assertThatJson(response)
        .whenIgnoringPaths("id")
        .isEqualTo(expectedResponse);
  }

  @Test
  @Order(5)
  @Sql(value = "/sql/user/init_three_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(value = "/sql/user/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("GET v1/users/99 throw NotFound 404 when user is not found")
  void findByOd_ThrowsNotFound_WhenUserIsNotFound() {
    String expectedResponse = fileUtils.readResourceFile("user/get-user-by-id-404.json");
    Long id = 99L;

    String response = RestAssured.given()
        .contentType(ContentType.JSON).accept(ContentType.JSON)
        .pathParam("id", id)
        .when()
        .get(URL + "/{id}")
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .log().all()
        .extract().response().body().asString();

    JsonAssertions.assertThatJson(response).isEqualTo(expectedResponse);
  }

  @Test
  @Order(6)
  @Sql(value = "/sql/user/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("POST v1/users creates a user")
  void save_CreatesUser_WhenSuccessful() {
    String request = fileUtils.readResourceFile("user/post-request-user-200.json");
    String expectedResponse = fileUtils.readResourceFile("user/post-response-user-201.json");

    String response = RestAssured.given()
        .contentType(ContentType.JSON).accept(ContentType.JSON)
        .header("X-api-key", "v1")
        .body(request)
        .when()
        .post(URL)
        .then()
        .statusCode(HttpStatus.CREATED.value())
        .log().all()
        .extract().response().body().asString();

    JsonAssertions.assertThatJson(response)
        .node("id")
        .asNumber()
        .isPositive();

    JsonAssertions.assertThatJson(response)
        .whenIgnoringPaths("id")
        .isEqualTo(expectedResponse);
  }

  @Test
  @Order(7)
  @Sql(value = "/sql/user/init_one_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(value = "/sql/user/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("PUT v1/users updates a user")
  void update_UpdatesUser_WhenSuccessful() throws Exception {
    String request = fileUtils.readResourceFile("user/put-request-user-200.json");
    List<User> users = repository.findByFirstNameIgnoreCase("Carlos");
    User oldUser = users.getFirst();

    Assertions.assertThat(users).isNotEmpty().hasSize(1);
    request = request.replace("1", users.getFirst().getId().toString());

    RestAssured.given()
        .contentType(ContentType.JSON).accept(ContentType.JSON)
        .body(request)
        .when()
        .put(URL)
        .then()
        .statusCode(HttpStatus.NO_CONTENT.value())
        .log().all();

    User updatedUser = repository.findById(oldUser.getId())
        .orElseThrow(() -> new NotFoundException("user not found"));
    String encryptedPassword = updatedUser.getPassword();

    Assertions.assertThat(passwordEncoder.matches("new_password", encryptedPassword)).isTrue();
  }

  @Test
  @Order(8)
  @Sql(value = "/sql/user/init_one_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(value = "/sql/user/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("PUT v1/users throw NotFound when user is not found")
  void update_ThrowNotFound_WhenUserIsNotFound() {
    String request = fileUtils.readResourceFile("user/put-request-user-404.json");
    String expectedResponse = fileUtils.readResourceFile("user/put-user-by-id-404.json");

    String response = RestAssured.given()
        .contentType(ContentType.JSON).accept(ContentType.JSON)
        .body(request)
        .when()
        .put(URL)
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .log().all()
        .extract().response().body().asString();

    JsonAssertions.assertThatJson(response).isEqualTo(expectedResponse);
  }

  @Test
  @Order(9)
  @Sql(value = "/sql/user/init_three_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(value = "/sql/user/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("DELETE v1/users/1 removes a user")
  void delete_RemovesUser_WhenSuccessful() {
    RestAssured.requestSpecification = requestSpecificationAdminUser;

    Long id = repository.findAll().getFirst().getId();

    RestAssured.given()
        .contentType(ContentType.JSON).accept(ContentType.JSON)
        .when()
        .pathParam("id", id)
        .delete(URL.concat("/{id}"))
        .then()
        .statusCode(HttpStatus.NO_CONTENT.value())
        .log().all();
  }

  @Test
  @Order(10)
  @Sql(value = "/sql/user/init_three_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(value = "/sql/user/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("DELETE v1/users/99 throw NotFound when user is not found")
  void delete_ThrowNotFound_WhenUserIsNotFound() {
    RestAssured.requestSpecification = requestSpecificationAdminUser;

    String expectedResponse = fileUtils.readResourceFile("user/delete-user-by-id-404.json");
    Long id = 99L;

    String response = RestAssured.given()
        .contentType(ContentType.JSON).accept(ContentType.JSON)
        .when()
        .pathParam("id", id)
        .delete(URL.concat("/{id}"))
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .log().all()
        .extract().response().body().asString();

    JsonAssertions.assertThatJson(response).isEqualTo(expectedResponse);
  }

  @Order(11)
  @ParameterizedTest
  @MethodSource("postUserBadRequestSource")
  @DisplayName("POST v1/users returns bad request when fields are invalid")
  void save_ReturnsBadRequest_WhenFieldsAreEmpty(String requestFile, String responseFile) {
    String request = fileUtils.readResourceFile("user/%s".formatted(requestFile));
    String expectedResponse = fileUtils.readResourceFile("user/%s".formatted(responseFile));

    String response = RestAssured.given()
        .contentType(ContentType.JSON).accept(ContentType.JSON)
        .header("X-api-key", "v1")
        .body(request)
        .when()
        .post(URL)
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .log().all()
        .extract().response().body().asString();

    JsonAssertions.assertThatJson(response)
        .whenIgnoringPaths("timestamp")
        .when(Option.IGNORING_ARRAY_ORDER)
        .isEqualTo(expectedResponse);
  }

  @Order(12)
  @ParameterizedTest
  @MethodSource("putUserBadRequestSource")
  @Sql(value = "/sql/user/init_one_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(value = "/sql/user/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("PUT v1/users returns bad request when fields are invalid")
  void update_ReturnsBadRequest_WhenFieldsAreEmpty(String requestFile, String responseFile) throws Exception {
    String request = fileUtils.readResourceFile("user/%s".formatted(requestFile));
    String expectedResponse = fileUtils.readResourceFile("user/%s".formatted(responseFile));

    String response = RestAssured.given()
        .contentType(ContentType.JSON).accept(ContentType.JSON)
        .body(request)
        .when()
        .put(URL)
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .log().all()
        .extract().response().body().asString();

    JsonAssertions.assertThatJson(response)
        .whenIgnoringPaths("timestamp")
        .when(Option.IGNORING_ARRAY_ORDER)
        .isEqualTo(expectedResponse);
  }

  private static Stream<Arguments> postUserBadRequestSource() {
    return Stream.of(
        Arguments.of("post-request-user-empty-fields-400.json", "post-response-user-empty-fields-400.json"),
        Arguments.of("post-request-user-blank-fields-400.json", "post-response-user-blank-fields-400.json"),
        Arguments.of("post-request-user-invalid-email-400.json", "post-response-user-invalid-email-400.json")
    );
  }

  private static Stream<Arguments> putUserBadRequestSource() {
    return Stream.of(
        Arguments.of("put-request-user-empty-fields-400.json", "put-response-user-empty-fields-400.json"),
        Arguments.of("put-request-user-blank-fields-400.json", "put-response-user-blank-fields-400.json"),
        Arguments.of("put-request-user-invalid-email-400.json", "put-response-user-invalid-email-400.json")
    );
  }

}
