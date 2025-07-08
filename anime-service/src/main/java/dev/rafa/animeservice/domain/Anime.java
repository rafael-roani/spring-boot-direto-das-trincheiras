package dev.rafa.animeservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Anime {

    private Long id;

    private String name;

    public static List<Anime> getAnimes() {
        List<Anime> animeList = new ArrayList<>();
        animeList.add(new Anime(1L, "One Piece"));
        animeList.add(new Anime(2L, "Naruto"));
        animeList.add(new Anime(3L, "Dragon Ball"));
        animeList.add(new Anime(4L, "Sword Art Online"));

        return animeList;
    }

}
