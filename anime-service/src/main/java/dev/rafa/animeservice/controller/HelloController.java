package dev.rafa.animeservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RestController
@RequestMapping("v1/greetings")
public class HelloController {

    @GetMapping(value = {"hi"})
    public String hi() {
        return "OMAE WA MOU SHINDE IRU";
    }

    @PostMapping
    public ResponseEntity<Long> save(@RequestBody String name) {
        log.info("Saving name '{}'", name);
        return ResponseEntity.ok(ThreadLocalRandom.current().nextLong(1, 100));
    }

}
