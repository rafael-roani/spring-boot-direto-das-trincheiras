package dev.rafa.animeservice.repository;

import dev.rafa.animeservice.domain.Producer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProducerHardCodedRepository {

    private static final List<Producer> PRODUCERS = new ArrayList<>();

    static {
        PRODUCERS.add(
                Producer.builder()
                        .id(1L)
                        .name("Mappa")
                        .createdAt(LocalDateTime.now())
                        .build()
        );
        PRODUCERS.add(
                Producer.builder()
                        .id(2L)
                        .name("Kyoto Animation")
                        .createdAt(LocalDateTime.now())
                        .build()
        );
        PRODUCERS.add(
                Producer.builder()
                        .id(3L)
                        .name("Madhouse")
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }

    public List<Producer> findAll() {
        return PRODUCERS;
    }

    public Optional<Producer> findById(Long id) {
        return PRODUCERS.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
    }

    public List<Producer> findByName(String name) {
        return PRODUCERS.stream()
                .filter(a -> a.getName().equalsIgnoreCase(name))
                .toList();
    }

    public void update(Producer producer) {
        delete(producer);
        save(producer);
    }

    public Producer save(Producer producer) {
        PRODUCERS.add(producer);
        return producer;
    }

    public void delete(Producer producer) {
        PRODUCERS.remove(producer);
    }

}
