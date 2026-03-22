package dev.rafa.userservice.config;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

import static dev.rafa.userservice.commons.Constants.*;

/**
 * @author Rafael Roani Gonçalves
 * @since 22/03/2026
 */
@Lazy
@TestConfiguration
public class RestAssuredConfig {

    @LocalServerPort
    int port;

    @Bean(name = "requestSpecificationRegularUser")
    public RequestSpecification requestSpecificationRegularUser() {
        return RestAssured.given()
                .baseUri(BASE_URI + port)
                .auth().preemptive().basic(REGULAR_USERNAME, PASSWORD);
    }

}
