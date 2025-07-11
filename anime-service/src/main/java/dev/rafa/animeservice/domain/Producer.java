package dev.rafa.animeservice.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Producer {

    private Long id;

    private String name;

    private LocalDateTime createdAt;

    @Getter
    private static List<Producer> producers = new ArrayList<>();

    static {
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
