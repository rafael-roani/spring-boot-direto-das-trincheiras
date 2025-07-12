package dev.rafa.animeservice.controller;

import dev.rafa.animeservice.domain.Anime;
import dev.rafa.animeservice.domain.Producer;
import dev.rafa.animeservice.mapper.AnimeMapper;
import dev.rafa.animeservice.request.AnimePostRequest;
import dev.rafa.animeservice.request.AnimePutRequest;
import dev.rafa.animeservice.response.AnimeGetResponse;
import dev.rafa.animeservice.response.AnimePostResponse;
import dev.rafa.animeservice.service.AnimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("v1/animes")
public class AnimeController {

    private static final AnimeMapper MAPPER = AnimeMapper.INSTANCE;

    private final AnimeService service;

    public AnimeController() {
        this.service = new AnimeService();
    }

    @GetMapping
    public ResponseEntity<List<AnimeGetResponse>> listAll(@RequestParam(required = false) String name) {
        log.debug("Request received to list all animes, param name '{}'", name);

        List<Anime> animes = service.findAll(name);

        List<AnimeGetResponse> response = MAPPER.toAnimeGetResponseList(animes);

        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<AnimeGetResponse> findById(@PathVariable Long id) {
        log.debug("Request to find anime by id: {}", id);

        Anime anime = service.findByIdOrThrowNotFound(id);
        AnimeGetResponse response = MAPPER.toAnimeGetResponse(anime);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<AnimePostResponse> save(@RequestBody AnimePostRequest animePostRequest) {
        log.debug("Request to save anime: {}", animePostRequest);

        Anime anime = MAPPER.toAnime(animePostRequest);

        Anime savedAnime = service.save(anime);

        AnimePostResponse response = MAPPER.toAnimePostResponse(savedAnime);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("Deleting anime by id: {}", id);

        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody AnimePutRequest request) {
        log.debug("Updating anime: {}", request);

        Anime animeToUpdated =  MAPPER.toAnime(request);

        service.update(animeToUpdated);

        return ResponseEntity.noContent().build();
    }

}
