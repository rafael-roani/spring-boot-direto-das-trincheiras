package dev.rafa.animeservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "database")
public class ConnectionConfigurationProperties {

    private String url;

    private String username;

    private String password;

}
