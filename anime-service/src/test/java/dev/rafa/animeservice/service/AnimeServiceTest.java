package dev.rafa.animeservice.service;

import dev.rafa.animeservice.domain.Anime;
import dev.rafa.animeservice.repository.AnimeHardCodeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnimeServiceTest {

    @InjectMocks
    private AnimeService service;

    @Mock
    private AnimeHardCodeRepository repository;

    private List<Anime> animesList;

    @BeforeEach
    void setUp() {
        animesList = new ArrayList<>();
        animesList.add(new Anime(1L, "Ful Metal Brotherhood"));
        animesList.add(new Anime(2L, "Steins Gate"));
        animesList.add(new Anime(3L, "Mashle"));
    }

    @Test
    @Order(1)
    @DisplayName("findAll returns a list with all animes when argument is null")
    void findAll_ReturnsAllAnimes_WhenArgumentIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(animesList);

        List<Anime> animes = service.findAll(null);
        Assertions.assertThat(animes)
                .isNotNull()
                .hasSize(animesList.size())
                .containsAll(this.animesList);
    }

    @Test
    @Order(2)
    @DisplayName("findAll returns list with found object when name is exists")
    void findAll_ReturnsFoundAnimeInList_WhenNameIsFound() {
        Anime anime = animesList.getFirst();
        List<Anime> expectedAnimes = Collections.singletonList(anime);

        BDDMockito.when(repository.findByName(anime.getName()))
                .thenReturn(expectedAnimes);

        List<Anime> animesFound = service.findAll(anime.getName());
        Assertions.assertThat(animesFound)
                .isNotNull()
                .isNotEmpty()
                .containsAll(expectedAnimes);
    }

    @Test
    @Order(3)
    @DisplayName("findAll returns empty list when name is not found")
    void findAll_ReturnsEmptyList_WhenNameIsNotFound() {
        String name = "not-found";
        BDDMockito.when(repository.findByName(name)).thenReturn(Collections.emptyList());

        List<Anime> animes = service.findAll(name);
        Assertions.assertThat(animes)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @Order(4)
    @DisplayName("findById returns an object with given id")
    void findAll_ReturnsAnimeById_WhenSuccessful() {
        Anime expectedAnime = animesList.getFirst();
        BDDMockito.when(repository.findById(expectedAnime.getId()))
                .thenReturn(java.util.Optional.of(expectedAnime));

        Anime anime = service.findByIdOrThrowNotFound(expectedAnime.getId());
        Assertions.assertThat(anime).isEqualTo(expectedAnime);
    }

    @Test
    @Order(5)
    @DisplayName("findById throw ResponseStatusException when anime is not found")
    void findByOd_ThrowsResponseStatusException_WhenAnimeIsNotFound() {
        Anime expectedAnime = animesList.getFirst();
        BDDMockito.when(repository.findById(expectedAnime.getId()))
                .thenReturn(java.util.Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.findByIdOrThrowNotFound(expectedAnime.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @Order(6)
    @DisplayName("save creates an anime")
    void save_CreatesAnime_WhenSuccessful() {
        Anime animeToSave = Anime.builder()
                .id(99L)
                .name("Pokemon")
                .build();

        BDDMockito.when(repository.save(animeToSave)).thenReturn(animeToSave);

        Anime savedAnime = service.save(animeToSave);
        Assertions.assertThat(savedAnime)
                .isNotNull()
                .isEqualTo(animeToSave)
                .hasNoNullFieldsOrProperties();
    }

    @Test
    @Order(7)
    @DisplayName("delete removes an anime")
    void delete_RemovesAnime_WhenSuccessful() {
        Anime animeToDelete = animesList.getFirst();
        BDDMockito.when(repository.findById(animeToDelete.getId()))
                .thenReturn(Optional.of(animeToDelete));
        BDDMockito.doNothing().when(repository).delete(animeToDelete);

        Assertions.assertThatNoException().isThrownBy(() -> service.delete(animeToDelete.getId()));
    }

    @Test
    @Order(8)
    @DisplayName("delete throw ResponseStatusException when anime is not found")
    void delete_ThrowResponseStatusException_WhenAnimeIsNotFound() {
        Anime animeToDelete = animesList.getFirst();

        BDDMockito.when(repository.findById(animeToDelete.getId()))
                .thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.delete(animeToDelete.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @Order(9)
    @DisplayName("update updates an anime")
    void update_UpdatesAnime_WhenSuccessful() {
        Anime animeToUpdate = animesList.getFirst();
        animeToUpdate.setName("Hellsing");

        BDDMockito.when(repository.findById(animeToUpdate.getId()))
                .thenReturn(Optional.of(animeToUpdate));
        BDDMockito.doNothing().when(repository).update(animeToUpdate);

        Assertions.assertThatNoException().isThrownBy(() -> service.update(animeToUpdate));
    }

    @Test
    @Order(10)
    @DisplayName("update throw ResponseStatusException when anime is not found")
    void update_ThrowResponseStatusException_WhenAnimeIsNotFound() {
        Anime animeToUpdate = animesList.getFirst();
        animeToUpdate.setName("Hellsing");

        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.update(animeToUpdate))
                .isInstanceOf(ResponseStatusException.class);
    }

}
