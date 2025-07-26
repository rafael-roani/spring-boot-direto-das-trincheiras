package dev.rafa.animeservice.commons;

import dev.rafa.animeservice.domain.Anime;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AnimeUtils {

    public List<Anime> newAnimeList() {
        List<Anime> animesList = new ArrayList<>();
        animesList.add(new Anime(1L, "Ful Metal Brotherhood"));
        animesList.add(new Anime(2L, "Steins Gate"));
        animesList.add(new Anime(3L, "Mashle"));

        return animesList;
    }

    public Anime newAnimeToSave() {
        return Anime.builder()
                .id(99L)
                .name("Overlord")
                .build();
    }

}
