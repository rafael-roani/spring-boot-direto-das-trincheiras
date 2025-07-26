package dev.rafa.animeservice.commons;

import dev.rafa.animeservice.domain.Producer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProducerUtils {

    public List<Producer> newProducerList() {
        String dateTime = "2025-07-19T17:54:51.1550735";
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime);

        Producer ufotable = Producer.builder()
                .id(1L)
                .name("Ufotable")
                .createdAt(localDateTime)
                .build();

        Producer witStudio = Producer.builder()
                .id(2L)
                .name("Wit Studio")
                .createdAt(localDateTime)
                .build();

        Producer studioGhibli = Producer.builder()
                .id(3L)
                .name("Studio Ghibli")
                .createdAt(localDateTime)
                .build();

        return new ArrayList<>(List.of(ufotable, witStudio, studioGhibli));
    }

    public Producer newProducerToSave() {
        return Producer.builder()
                .id(99L)
                .name("MAPPA")
                .createdAt(LocalDateTime.now())
                .build();
    }
}
