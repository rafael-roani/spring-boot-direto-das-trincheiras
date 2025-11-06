package dev.rafa.animeservice.controller;

import dev.rafa.animeservice.domain.Anime;
import dev.rafa.animeservice.mapper.AnimeMapper;
import dev.rafa.animeservice.request.AnimePostRequest;
import dev.rafa.animeservice.request.AnimePutRequest;
import dev.rafa.animeservice.response.AnimeGetResponse;
import dev.rafa.animeservice.response.AnimePostResponse;
import dev.rafa.animeservice.service.AnimeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/animes")
public class AnimeController {

    private final AnimeMapper mapper;

    private final AnimeService service;

    @GetMapping
    public ResponseEntity<List<AnimeGetResponse>> findAll(@RequestParam(required = false) String name) {
        log.debug("Request received to list all animes, param name '{}'", name);

        List<Anime> animes = service.findAll(name);

        List<AnimeGetResponse> response = mapper.toAnimeGetResponseList(animes);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<AnimeGetResponse>> findAllPaginated(Pageable pageable) {
        log.debug("Request received to list all paginated");

        Page<AnimeGetResponse> pageAnimeGetResponse = service.findAllPaginated(pageable)
                .map(mapper::toAnimeGetResponse);
        return ResponseEntity.ok(pageAnimeGetResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<AnimeGetResponse> findById(@PathVariable Long id) {
        log.debug("Request to find anime by id: {}", id);

        Anime anime = service.findByIdOrThrowNotFound(id);
        AnimeGetResponse response = mapper.toAnimeGetResponse(anime);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<AnimePostResponse> save(@RequestBody @Valid AnimePostRequest animePostRequest) {
        log.debug("Request to save anime: {}", animePostRequest);

        Anime anime = mapper.toAnime(animePostRequest);

        Anime savedAnime = service.save(anime);

        AnimePostResponse response = mapper.toAnimePostResponse(savedAnime);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("Deleting anime by id: {}", id);

        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid AnimePutRequest request) {
        log.debug("Updating anime: {}", request);

        Anime animeToUpdated = mapper.toAnime(request);

        service.update(animeToUpdated);

        return ResponseEntity.noContent().build();
    }

}
