package dev.rafa.animeservice.controller;

import dev.rafa.animeservice.domain.Anime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("v1/animes")
public class AnimeController {

    @GetMapping
    public ResponseEntity<List<Anime>> listAll(@RequestParam(required = false) String name) {
        List<Anime> animes;

        if (name == null) {
            animes = Anime.getAnimes();
        } else {
            animes = Anime.getAnimes()
                    .stream()
                    .filter(a -> a.getName().equalsIgnoreCase(name))
                    .toList();
        }

        return ResponseEntity.ok(animes);
    }

    @GetMapping("{id}")
    public ResponseEntity<Anime> findById(@PathVariable Long id) {
        return ResponseEntity.ok(Anime.getAnimes()
                .stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null)
        );
    }

}
