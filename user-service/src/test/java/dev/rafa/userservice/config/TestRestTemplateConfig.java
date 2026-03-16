package dev.rafa.userservice.config;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.util.DefaultUriBuilderFactory;

/**
 * @author Rafael Roani Gonçalves
 * @since 15/03/2026
 */
@Lazy
@Configuration
public class TestRestTemplateConfig {

    @LocalServerPort
    private int port;

    @Bean
    public TestRestTemplate testRestTemplate() {
        DefaultUriBuilderFactory uri = new DefaultUriBuilderFactory("http://localhost:" + port);

        TestRestTemplate testRestTemplate = new TestRestTemplate()
                .withBasicAuth("mestre.kame@dbz.com", "test");

        testRestTemplate.setUriTemplateHandler(uri);

        return testRestTemplate;
    }

}
