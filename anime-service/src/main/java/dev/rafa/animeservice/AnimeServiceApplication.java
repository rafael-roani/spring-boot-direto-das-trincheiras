package dev.rafa.animeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"dev.rafa"})
public class AnimeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnimeServiceApplication.class, args);
    }

}
