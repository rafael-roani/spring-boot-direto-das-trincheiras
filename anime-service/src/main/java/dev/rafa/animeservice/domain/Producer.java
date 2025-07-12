package dev.rafa.animeservice.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@Builder
@EqualsAndHashCode(of = "id")
public class Producer {

    private Long id;

    private String name;

    private LocalDateTime createdAt;

}
