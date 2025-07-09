package dev.rafa.animeservice.controller;

import dev.rafa.animeservice.domain.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RestController
@RequestMapping("v1/producers")
public class ProducerController {

    @GetMapping
    public ResponseEntity<List<Producer>> listAll(@RequestParam(required = false) String name) {
        List<Producer> producers;

        if (name == null) {
            producers = Producer.getProducers();
        } else {
            producers = Producer.getProducers()
                    .stream()
                    .filter(a -> a.getName().equalsIgnoreCase(name))
                    .toList();
        }

        return ResponseEntity.ok(producers);
    }

    @GetMapping("{id}")
    public ResponseEntity<Producer> findById(@PathVariable Long id) {
        return ResponseEntity.ok(Producer.getProducers()
                .stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null)
        );
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE,
            headers = "X-api-key")
    public ResponseEntity<Producer> save(@RequestBody Producer producer, @RequestHeader HttpHeaders headers) {
        log.info("headers: {}", headers.toSingleValueMap());
        producer.setId(ThreadLocalRandom.current().nextLong(1, 100_000));
        Producer.getProducers().add(producer);

        return ResponseEntity.ok(producer);
    }

}
