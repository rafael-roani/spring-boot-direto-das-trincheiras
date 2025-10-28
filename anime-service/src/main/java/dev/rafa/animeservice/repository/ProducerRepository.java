package dev.rafa.animeservice.repository;

import dev.rafa.animeservice.domain.Producer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProducerRepository extends JpaRepository<Producer, Long> {

    List<Producer> findByName(String name);

}
