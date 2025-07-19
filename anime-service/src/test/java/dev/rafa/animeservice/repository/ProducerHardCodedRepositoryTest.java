package dev.rafa.animeservice.repository;

import dev.rafa.animeservice.domain.Producer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProducerHardCodedRepositoryTest {

    @InjectMocks
    private ProducerHardCodedRepository repository;

    @Mock
    private ProducerData producerData;

    private final List<Producer> producerList = new ArrayList<>();

    @BeforeEach
    void init() {
        Producer ufotable = Producer.builder()
                .id(1L)
                .name("Ufotable")
                .createdAt(LocalDateTime.now())
                .build();

        Producer witStudio = Producer.builder()
                .id(2L)
                .name("Wit Studio")
                .createdAt(LocalDateTime.now())
                .build();

        Producer studioGhibli = Producer.builder()
                .id(3L)
                .name("Studio Ghibli")
                .createdAt(LocalDateTime.now())
                .build();

        producerList.addAll(List.of(ufotable, witStudio, studioGhibli));
    }

    @Test
    @Order(1)
    @DisplayName("findAll returns a list with all producers")
    void findAll_ReturnsAllProducers_WhenSuccessful() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        List<Producer> producers = repository.findAll();
        Assertions.assertThat(producers)
                .isNotNull()
                .hasSize(producerList.size())
                .containsAll(this.producerList);

    }

    @Test
    @Order(2)
    @DisplayName("findById returns an object with given id")
    void findAll_ReturnsProducerById_WhenSuccessful() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        Producer expectedProducer = producerList.getFirst();
        Optional<Producer> producer = repository.findById(expectedProducer.getId());

        Assertions.assertThat(producer)
                .isPresent()
                .contains(expectedProducer);
    }

    @Test
    @Order(3)
    @DisplayName("findByName returns empty list when name is null")
    void findById_ReturnsEmptyList_WhenNameIsNull() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        List<Producer> producers = repository.findByName(null);
        Assertions.assertThat(producers)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @Order(4)
    @DisplayName("findByName returns list with found object when name is exists")
    void findById_ReturnsFoundProducerInList_WhenNameIsFound() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        Producer expectedProducer = producerList.getFirst();
        List<Producer> producers = repository.findByName(expectedProducer.getName());
        Assertions.assertThat(producers)
                .isNotNull()
                .isNotEmpty()
                .contains(expectedProducer);
    }

    @Test
    @Order(5)
    @DisplayName("save creates a producer")
    void save_CreatesProducer_WhenSuccessful() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        Producer producerToSave = Producer.builder()
                .id(99L)
                .name("MAPPA")
                .createdAt(LocalDateTime.now())
                .build();

        Producer producer = repository.save(producerToSave);
        Assertions.assertThat(producer)
                .isNotNull()
                .isEqualTo(producerToSave)
                .hasNoNullFieldsOrProperties();

        Optional<Producer> producerSaved = repository.findById(producerToSave.getId());
        Assertions.assertThat(producerSaved)
                .isPresent()
                .contains(producerToSave);
    }

    @Test
    @Order(6)
    @DisplayName("delete removes a producer")
    void delete_RemovesProducer_WhenSuccessful() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        Producer producerToDelete = producerList.getFirst();
        repository.delete(producerToDelete);

        List<Producer> producers = repository.findAll();
        Assertions.assertThat(producers).doesNotContain(producerToDelete);
    }

    @Test
    @Order(7)
    @DisplayName("update updates a producer")
    void update_UpdatesProducer_WhenSuccessful() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        Producer producerToUpdate = producerList.getFirst();
        producerToUpdate.setName("Aniplex");

        repository.update(producerToUpdate);

        Assertions.assertThat(this.producerList).contains(producerToUpdate);

        Optional<Producer> producerUpdated = repository.findById(producerToUpdate.getId());

        Assertions.assertThat(producerUpdated).isPresent();
        Assertions.assertThat(producerUpdated.get().getName())
                .isEqualTo(producerToUpdate.getName());
    }

}
