package dev.rafa.animeservice.controller;

import dev.rafa.animeservice.commons.AnimeUtils;
import dev.rafa.animeservice.commons.FileUtils;
import dev.rafa.animeservice.domain.Anime;
import dev.rafa.animeservice.repository.AnimeRepository;
import dev.rafa.animeservice.repository.ProducerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebMvcTest(controllers = AnimeController.class)
@ComponentScan(basePackages = {"dev.rafa.animeservice"})
class AnimeControllerTest {

    private static final String URL = "/v1/animes";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProducerRepository producerRepository;

    @MockBean
    private AnimeRepository repository;

    private List<Anime> animesList;

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    private AnimeUtils animeUtils;

    @BeforeEach
    void init() {
        animesList = animeUtils.newAnimeList();
    }

    @Test
    @Order(1)
    @DisplayName("GET v1/animes returns a list with all animes when argument is null")
    void findAll_ReturnsAllAnimes_WhenArgumentIsNull() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(animesList);
        String response = fileUtils.readResourceFile("anime/get-anime-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(2)
    @DisplayName("GET v1/animes?name=Mashle returns a list with found object when name exists")
    void findAll_ReturnsFoundAnimeList_WhenNameIsFound() throws Exception {
        String response = fileUtils.readResourceFile("anime/get-anime-mashle-name-200.json");
        String name = "Mashle";
        Anime mashle = animesList.stream()
                .filter(anime -> anime.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
        BDDMockito.when(repository.findByName(name)).thenReturn(Collections.singletonList(mashle));

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(3)
    @DisplayName("GET v1/animes?name=x returns empty list when name is not found")
    void findAll_ReturnsEmptyList_WhenNameIsNotFound() throws Exception {
        String response = fileUtils.readResourceFile("anime/get-anime-x-name-200.json");
        String name = "x";

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(4)
    @DisplayName("GET v1/animes/1 returns an object with given id")
    void findById_ReturnsAnimeById_WhenSuccessful() throws Exception {
        String response = fileUtils.readResourceFile("anime/get-anime-by-id-200.json");
        Long id = 1L;
        Optional<Anime> foundAnime = animesList.stream()
                .filter(anime -> anime.getId().equals(id))
                .findFirst();
        BDDMockito.when(repository.findById(id)).thenReturn(foundAnime);

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(5)
    @DisplayName("GET v1/animes/99 throw NotFound 404 when anime is not found")
    void findByOd_ThrowsNotFound_WhenAnimeIsNotFound() throws Exception {
        String response = fileUtils.readResourceFile("anime/get-anime-by-id-404.json");
        Long id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(6)
    @DisplayName("POST v1/animes creates a anime")
    void save_CreatesAnime_WhenSuccessful() throws Exception {
        String request = fileUtils.readResourceFile("anime/post-request-anime-200.json");
        String response = fileUtils.readResourceFile("anime/post-response-anime-201.json");

        Anime animeToSave = animeUtils.newAnimeToSave();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(animeToSave);

        mockMvc.perform(MockMvcRequestBuilders
                                .post(URL)
                                .content(request)
                                .header("X-api-key", "v1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(7)
    @DisplayName("PUT v1/animes updates a anime")
    void update_UpdatesAnime_WhenSuccessful() throws Exception {
        String request = fileUtils.readResourceFile("anime/put-request-anime-200.json");
        Long id = 1L;
        Optional<Anime> foundAnime = animesList.stream()
                .filter(anime -> anime.getId().equals(id))
                .findFirst();
        BDDMockito.when(repository.findById(id)).thenReturn(foundAnime);

        mockMvc.perform(MockMvcRequestBuilders
                                .put(URL)
                                .content(request)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @Order(8)
    @DisplayName("PUT v1/animes throw NotFound when anime is not found")
    void update_ThrowNotFound_WhenAnimeIsNotFound() throws Exception {
        String request = fileUtils.readResourceFile("anime/put-request-anime-404.json");
        String response = fileUtils.readResourceFile("anime/put-anime-by-id-404.json");

        mockMvc.perform(MockMvcRequestBuilders
                                .put(URL)
                                .content(request)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(9)
    @DisplayName("DELETE v1/animes/1 removes a anime")
    void delete_RemovesAnime_WhenSuccessful() throws Exception {
        Long id = animesList.getFirst().getId();
        Optional<Anime> foundAnime = animesList.stream()
                .filter(anime -> anime.getId().equals(id))
                .findFirst();
        BDDMockito.when(repository.findById(id)).thenReturn(foundAnime);

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @Order(10)
    @DisplayName("DELETE v1/animes/99 throw NotFound when anime is not found")
    void delete_ThrowNotFound_WhenAnimeIsNotFound() throws Exception {
        String response = fileUtils.readResourceFile("anime/delete-anime-by-id-404.json");
        Long id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Order(11)
    @ParameterizedTest
    @MethodSource("postAnimeBadRequestSource")
    @DisplayName("POST v1/animes returns bad request when fields are invalid")
    void save_ReturnsBadRequest_WhenFieldsAreEmpty(String fileName, List<String> errors) throws Exception {
        String request = fileUtils.readResourceFile("anime/%s".formatted(fileName));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                                                      .post(URL)
                                                      .content(request)
                                                      .header("X-api-key", "v1")
                                                      .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        Exception resolvedException = mvcResult.getResolvedException();

        org.assertj.core.api.Assertions.assertThat(resolvedException).isNotNull();

        org.assertj.core.api.Assertions.assertThat(resolvedException.getMessage())
                .contains(errors);
    }

    @Order(12)
    @ParameterizedTest
    @MethodSource("putAnimeBadRequestSource")
    @DisplayName("PUT v1/animes returns bad request when fields are invalid")
    void update_ReturnsBadRequest_WhenFieldsAreEmpty(String fileName, List<String> errors) throws Exception {
        String request = fileUtils.readResourceFile("anime/%s".formatted(fileName));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                                                      .put(URL)
                                                      .content(request)
                                                      .header("X-api-key", "v1")
                                                      .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        Exception resolvedException = mvcResult.getResolvedException();

        Assertions.assertThat(resolvedException).isNotNull();

        Assertions.assertThat(resolvedException.getMessage())
                .contains(errors);
    }

    private static Stream<Arguments> postAnimeBadRequestSource() {
        List<String> allRequiredErrors = allRequiredErrors();

        return Stream.of(
                Arguments.of("post-request-anime-empty-fields-400.json", allRequiredErrors),
                Arguments.of("post-request-anime-blank-fields-400.json", allRequiredErrors)
        );
    }

    private static Stream<Arguments> putAnimeBadRequestSource() {
        List<String> allRequiredErrors = allRequiredErrors();

        allRequiredErrors.add("The field 'id' cannot be null");

        return Stream.of(
                Arguments.of("put-request-anime-empty-fields-400.json", allRequiredErrors),
                Arguments.of("put-request-anime-blank-fields-400.json", allRequiredErrors)
        );
    }

    private static List<String> allRequiredErrors() {
        String nameRequiredError = "The field 'name' is required";

        return new ArrayList<>(List.of(nameRequiredError));
    }

}
