package dev.rafa.animeservice.repository;

import dev.rafa.animeservice.domain.Anime;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AnimeHardCodeRepository {

    private static final List<Anime> ANIMES = new ArrayList<>();

    static {
        ANIMES.add(new Anime(1L, "One Piece"));
        ANIMES.add(new Anime(2L, "Naruto"));
        ANIMES.add(new Anime(3L, "Dragon Ball"));
        ANIMES.add(new Anime(4L, "Sword Art Online"));
    }

    public List<Anime> findAll() {
        return ANIMES;
    }

    public Optional<Anime> findById(Long id) {
        return ANIMES.stream()
                .filter(anime -> anime.getId().equals(id))
                .findFirst();
    }

    public List<Anime> findByName(String name) {
        return ANIMES.stream()
                .filter(anime -> anime.getName().equalsIgnoreCase(name))
                .toList();
    }

    public void update(Anime anime) {
        delete(anime);
        save(anime);
    }

    public Anime save(Anime anime) {
        ANIMES.add(anime);
        return anime;
    }

    public void delete(Anime anime) {
        ANIMES.remove(anime);
    }

}
