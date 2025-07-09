package dev.rafa.animeservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producer {

    private Long id;

    private String name;

    @Getter
    private static List<Producer> producers = new ArrayList<>();

    static {
        producers.add(new Producer(1L, "Mappa"));
        producers.add(new Producer(2L, "Kyoto Animation"));
        producers.add(new Producer(3L, "Madhouse"));
    }

}
