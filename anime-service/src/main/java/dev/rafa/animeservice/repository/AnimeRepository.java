package dev.rafa.animeservice.repository;

import dev.rafa.animeservice.domain.Anime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Long> {

  List<Anime> findByName(String name);

}
