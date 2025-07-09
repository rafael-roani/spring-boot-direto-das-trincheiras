package dev.rafa.animeservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Anime {

    private Long id;

    private String name;

    @Getter
    private static List<Anime> animes = new ArrayList<>();

    static {
        animes.add(new Anime(1L, "One Piece"));
        animes.add(new Anime(2L, "Naruto"));
        animes.add(new Anime(3L, "Dragon Ball"));
        animes.add(new Anime(4L, "Sword Art Online"));
    }

}
