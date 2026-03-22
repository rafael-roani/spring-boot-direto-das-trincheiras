package dev.rafa.userservice.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.util.DefaultUriBuilderFactory;

import static dev.rafa.userservice.commons.Constants.*;

/**
 * @author Rafael Roani Gonçalves
 * @since 15/03/2026
 */
@Lazy
@TestConfiguration
public class TestRestTemplateConfig {

    @LocalServerPort
    private int port;

    @Bean
    public TestRestTemplate testRestTemplate() {
        DefaultUriBuilderFactory uri = new DefaultUriBuilderFactory(BASE_URI + port);

        TestRestTemplate testRestTemplate = new TestRestTemplate()
                .withBasicAuth(REGULAR_USERNAME, PASSWORD);

        testRestTemplate.setUriTemplateHandler(uri);

        return testRestTemplate;
    }

}
