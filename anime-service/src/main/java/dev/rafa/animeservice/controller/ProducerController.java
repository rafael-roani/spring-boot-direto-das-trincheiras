package dev.rafa.animeservice.controller;

import dev.rafa.animeservice.domain.Producer;
import dev.rafa.animeservice.mapper.ProducerMapper;
import dev.rafa.animeservice.request.ProducerPostRequest;
import dev.rafa.animeservice.request.ProducerPutRequest;
import dev.rafa.animeservice.response.ProducerGetResponse;
import dev.rafa.animeservice.response.ProducerPostResponse;
import dev.rafa.animeservice.service.ProducerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/producers")
public class ProducerController {

    private final ProducerMapper mapper;

    private final ProducerService service;

    @GetMapping
    public ResponseEntity<List<ProducerGetResponse>> findAll(@RequestParam(required = false) String name) {
        log.debug("Request received to list all producers, param name '{}'", name);

        List<Producer> producers = service.findAll(name);

        List<ProducerGetResponse> response = mapper.toProducerGetResponseList(producers);

        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProducerGetResponse> findById(@PathVariable Long id) {
        log.debug("Request to find producer by id: {}", id);

        Producer producer = service.findByIdOrThrowNotFound(id);
        ProducerGetResponse response = mapper.toProducerGetResponse(producer);

        return ResponseEntity.ok(response);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE,
            headers = "X-api-key")
    public ResponseEntity<ProducerPostResponse> save(@RequestBody @Valid ProducerPostRequest producerPostRequest, @RequestHeader HttpHeaders headers) {
        log.info("headers: {}", headers.toSingleValueMap());
        log.debug("Saving producer: {}", producerPostRequest);

        Producer producer = mapper.toProducer(producerPostRequest);

        Producer savedProducer = service.save(producer);

        ProducerPostResponse response = mapper.toProducerPostResponse(savedProducer);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("Deleting producer by id: {}", id);

        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid ProducerPutRequest request) {
        log.debug("Updating producer: {}", request);

        Producer producerToUpdate = mapper.toProducer(request);

        service.update(producerToUpdate);

        return ResponseEntity.noContent().build();
    }

}
