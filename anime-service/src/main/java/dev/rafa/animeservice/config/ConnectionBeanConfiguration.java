package dev.rafa.animeservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
public class ConnectionBeanConfiguration {

    private final ConnectionConfigurationProperties configurationProperties;

    @Bean
    // @Primary
    @Profile({"mysql", "test"})
    public Connection connectionMySql() {
        return new Connection(
                configurationProperties.getUrl(),
                configurationProperties.getUsername(),
                configurationProperties.getPassword()
        );
    }

    @Bean
    @Profile("mongo")
    public Connection connectionMongo() {
        return new Connection(
                configurationProperties.getUrl(),
                configurationProperties.getUsername(),
                configurationProperties.getPassword()
        );
    }

}
