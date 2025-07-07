package dev.rafa.animeservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("v1/animes")
public class AnimeController {

    @GetMapping
    public ResponseEntity<List<String>> listAll() {
        return ResponseEntity.ok(List.of("One Piece", "Naruto"));
    }

}
