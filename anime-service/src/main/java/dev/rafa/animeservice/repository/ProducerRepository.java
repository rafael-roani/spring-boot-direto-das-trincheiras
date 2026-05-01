package dev.rafa.animeservice.repository;

import dev.rafa.animeservice.domain.Producer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProducerRepository extends JpaRepository<Producer, Long> {

  List<Producer> findByName(String name);

}
