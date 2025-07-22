package dev.rafa.animeservice.service;

import dev.rafa.animeservice.domain.Producer;
import dev.rafa.animeservice.repository.ProducerHardCodedRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProducerServiceTest {

    @InjectMocks
    private ProducerService service;

    @Mock
    private ProducerHardCodedRepository repository;

    private List<Producer> producerList;

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

        producerList = new ArrayList<>(List.of(ufotable, witStudio, studioGhibli));
    }

    @Test
    @Order(1)
    @DisplayName("findAll returns a list with all producers when argument is null")
    void findAll_ReturnsAllProducers_WhenArgumentIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(producerList);

        List<Producer> producers = service.findAll(null);
        Assertions.assertThat(producers)
                .isNotNull()
                .hasSize(producerList.size())
                .containsAll(this.producerList);
    }

    @Test
    @Order(2)
    @DisplayName("findAll returns list with found object when name is exists")
    void findAll_ReturnsFoundProducerInList_WhenNameIsFound() {
        Producer producer = producerList.getFirst();
        List<Producer> expectedProducers = Collections.singletonList(producer);

        BDDMockito.when(repository.findByName(producer.getName()))
                .thenReturn(expectedProducers);

        List<Producer> producersFound = service.findAll(producer.getName());
        Assertions.assertThat(producersFound)
                .isNotNull()
                .isNotEmpty()
                .containsAll(expectedProducers);
    }

    @Test
    @Order(3)
    @DisplayName("findAll returns empty list when name is not found")
    void findAll_ReturnsEmptyList_WhenNameIsNotFound() {
        String name = "not-found";
        BDDMockito.when(repository.findByName(name)).thenReturn(Collections.emptyList());

        List<Producer> producers = service.findAll(name);
        Assertions.assertThat(producers)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @Order(4)
    @DisplayName("findById returns an object with given id")
    void findById_ReturnsProducerById_WhenSuccessful() {
        Producer expectedProducer = producerList.getFirst();
        BDDMockito.when(repository.findById(expectedProducer.getId()))
                .thenReturn(Optional.of(expectedProducer));

        Producer producer = service.findByIdOrThrowNotFound(expectedProducer.getId());

        Assertions.assertThat(producer).isEqualTo(expectedProducer);
    }

    @Test
    @Order(5)
    @DisplayName("findById throw ResponseStatusException when producer is not found")
    void findByOd_ThrowsResponseStatusException_WhenProducerIsNotFound() {
        Producer expectedProducer = producerList.getFirst();
        BDDMockito.when(repository.findById(expectedProducer.getId()))
                .thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.findByIdOrThrowNotFound(expectedProducer.getId()))
                .isInstanceOf(ResponseStatusException.class);

    }

    @Test
    @Order(6)
    @DisplayName("save creates a producer")
    void save_CreatesProducer_WhenSuccessful() {
        Producer producerToSave = Producer.builder()
                .id(99L)
                .name("MAPPA")
                .createdAt(LocalDateTime.now())
                .build();

        BDDMockito.when(repository.save(producerToSave)).thenReturn(producerToSave);

        Producer savedProducer = service.save(producerToSave);
        Assertions.assertThat(savedProducer)
                .isNotNull()
                .isEqualTo(producerToSave)
                .hasNoNullFieldsOrProperties();
    }

    @Test
    @Order(7)
    @DisplayName("delete removes a producer")
    void delete_RemovesProducer_WhenSuccessful() {
        Producer producerToDelete = producerList.getFirst();
        BDDMockito.when(repository.findById(producerToDelete.getId()))
                .thenReturn(Optional.of(producerToDelete));
        BDDMockito.doNothing().when(repository).delete(producerToDelete);

        Assertions.assertThatNoException().isThrownBy(() -> service.delete(producerToDelete.getId()));
    }

    @Test
    @Order(8)
    @DisplayName("delete throw ResponseStatusException when producer is not found")
    void delete_ThrowResponseStatusException_WhenProducerIsNotFound() {
        Producer producerToDelete = producerList.getFirst();

        BDDMockito.when(repository.findById(producerToDelete.getId()))
                .thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.delete(producerToDelete.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @Order(9)
    @DisplayName("update updates a producer")
    void update_UpdatesProducer_WhenSuccessful() {
        Producer producerToUpdate = producerList.getFirst();
        producerToUpdate.setName("Aniplex");

        BDDMockito.when(repository.findById(producerToUpdate.getId()))
                .thenReturn(Optional.of(producerToUpdate));
        BDDMockito.doNothing().when(repository).update(producerToUpdate);

        Assertions.assertThatNoException().isThrownBy(() -> service.update(producerToUpdate));
    }

    @Test
    @Order(10)
    @DisplayName("update throw ResponseStatusException when producer is not found")
    void update_ThrowResponseStatusException_WhenProducerIsNotFound() {
        Producer producerToUpdate = producerList.getFirst();
        producerToUpdate.setName("Aniplex");

        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.update(producerToUpdate))
                .isInstanceOf(ResponseStatusException.class);
    }

}
