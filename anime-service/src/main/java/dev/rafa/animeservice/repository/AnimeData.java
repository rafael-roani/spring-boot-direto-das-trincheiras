package dev.rafa.animeservice.repository;

import dev.rafa.animeservice.domain.Anime;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Component
public class AnimeData {

    private final List<Anime> animes = new ArrayList<>();

    {
        animes.add(new Anime(1L, "One Piece"));
        animes.add(new Anime(2L, "Naruto"));
        animes.add(new Anime(3L, "Dragon Ball"));
        animes.add(new Anime(4L, "Sword Art Online"));
    }

}
