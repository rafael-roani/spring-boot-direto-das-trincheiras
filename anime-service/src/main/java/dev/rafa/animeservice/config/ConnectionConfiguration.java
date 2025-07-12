package dev.rafa.animeservice.config;

import external.dependency.Connection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ConnectionConfiguration {

    @Bean
    // @Primary
    public Connection connectionMySql() {
        return new Connection("localhost", "devdojoMySql", "goku");
    }

    @Bean
    public Connection connectionMongo() {
        return new Connection("localhost", "devdojoMongo", "goku");
    }

}
