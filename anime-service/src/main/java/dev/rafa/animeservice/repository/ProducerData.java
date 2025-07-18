package dev.rafa.animeservice.repository;

import dev.rafa.animeservice.domain.Producer;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Component
public class ProducerData {

    private final List<Producer> producers = new ArrayList<>();

    {
        producers.add(
                Producer.builder()
                        .id(1L)
                        .name("Mappa")
                        .createdAt(LocalDateTime.now())
                        .build()
        );
        producers.add(
                Producer.builder()
                        .id(2L)
                        .name("Kyoto Animation")
                        .createdAt(LocalDateTime.now())
                        .build()
        );
        producers.add(
                Producer.builder()
                        .id(3L)
                        .name("Madhouse")
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }

}
