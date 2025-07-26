package dev.rafa.animeservice.repository;

import dev.rafa.animeservice.commons.AnimeUtils;
import dev.rafa.animeservice.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnimeHardCodedRepositoryTest {

    @InjectMocks
    private AnimeHardCodedRepository repository;

    @Mock
    private AnimeData animeData;

    private List<Anime> animeList;

    @InjectMocks
    private AnimeUtils animeUtils;

    @BeforeEach
    void init() {
        animeList = animeUtils.newAnimeList();
    }

    @Test
    @Order(1)
    @DisplayName("findAll returns a list with all animes")
    void findAll_ReturnsAllAnimes_WhenSuccessful() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        List<Anime> animes = repository.findAll();
        Assertions.assertThat(animes)
                .isNotNull()
                .hasSize(animeList.size())
                .containsAll(this.animeList);
    }

    @Test
    @Order(2)
    @DisplayName("findById returns an anime with given id")
    void findAll_ReturnsAnimeById_WhenSuccessful() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        Anime expectedAnime = animeList.getFirst();
        Optional<Anime> anime = repository.findById(expectedAnime.getId());

        Assertions.assertThat(anime)
                .isPresent()
                .contains(expectedAnime);
    }

    @Test
    @Order(3)
    @DisplayName("findByName returns empty list when name is null")
    void findById_ReturnsEmptyList_WhenNameIsNull() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        List<Anime> animes = repository.findByName(null);
        Assertions.assertThat(animes)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @Order(4)
    @DisplayName("findByName returns list with found object when name is exists")
    void findById_ReturnsFoundAnimeInList_WhenNameIsFound() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        Anime expectedAnime = animeList.getFirst();
        List<Anime> animes = repository.findByName(expectedAnime.getName());
        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .contains(expectedAnime);
    }

    @Test
    @Order(5)
    @DisplayName("save creates an anime")
    void save_CreatesAnime_WhenSuccessful() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        Anime animeToSave = animeUtils.newAnimeToSave();

        Anime anime = repository.save(animeToSave);
        Assertions.assertThat(anime)
                .isNotNull()
                .isEqualTo(animeToSave)
                .hasNoNullFieldsOrProperties();

        Optional<Anime> animeSaved = repository.findById(animeToSave.getId());
        Assertions.assertThat(animeSaved)
                .isPresent()
                .contains(animeToSave);
    }

    @Test
    @Order(6)
    @DisplayName("delete removes an anime")
    void delete_RemovesAnime_WhenSuccessful() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        Anime animeToDelete = animeList.getFirst();
        repository.delete(animeToDelete);

        List<Anime> animes = repository.findAll();
        Assertions.assertThat(animes).doesNotContain(animeToDelete);
    }

    @Test
    @Order(7)
    @DisplayName("update updates an anime")
    void update_UpdatesAnime_WhenSuccessful() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        Anime animeToUpdate = animeList.getFirst();
        animeToUpdate.setName("Hellsing");

        repository.update(animeToUpdate);

        Assertions.assertThat(this.animeList).contains(animeToUpdate);

        Optional<Anime> animeUpdated = repository.findById(animeToUpdate.getId());

        Assertions.assertThat(animeUpdated).isPresent();
        Assertions.assertThat(animeUpdated.get().getName())
                .isEqualTo(animeToUpdate.getName());
    }

}
