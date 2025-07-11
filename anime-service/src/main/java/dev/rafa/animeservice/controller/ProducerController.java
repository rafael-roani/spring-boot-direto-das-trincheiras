package dev.rafa.animeservice.controller;

import dev.rafa.animeservice.domain.Producer;
import dev.rafa.animeservice.mapper.ProducerMapper;
import dev.rafa.animeservice.request.ProducerPostRequest;
import dev.rafa.animeservice.response.ProducerGetResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RestController
@RequestMapping("v1/producers")
public class ProducerController {

    private static final ProducerMapper MAPPER = ProducerMapper.INSTANCE;

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
    public ResponseEntity<ProducerGetResponse> save(@RequestBody ProducerPostRequest producerPostRequest, @RequestHeader HttpHeaders headers) {
        log.info("headers: {}", headers.toSingleValueMap());

        Producer producer = MAPPER.toProducer(producerPostRequest);
        ProducerGetResponse response = MAPPER.toProducerGetResponse(producer);

        Producer.getProducers().add(producer);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
