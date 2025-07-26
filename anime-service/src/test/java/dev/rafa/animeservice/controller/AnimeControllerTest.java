package dev.rafa.animeservice.controller;

import dev.rafa.animeservice.commons.FileUtils;
import dev.rafa.animeservice.domain.Anime;
import dev.rafa.animeservice.repository.AnimeData;
import dev.rafa.animeservice.repository.AnimeHardCodedRepository;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebMvcTest(controllers = AnimeController.class)
@ComponentScan(basePackages = {"dev.rafa.animeservice"})
class AnimeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnimeData animeData;

    @SpyBean
    private AnimeHardCodedRepository repository;

    private List<Anime> animesList;

    @Autowired
    private FileUtils fileUtils;

    @BeforeEach
    void init() {
        animesList = new ArrayList<>();
        animesList.add(new Anime(1L, "Ful Metal Brotherhood"));
        animesList.add(new Anime(2L, "Steins Gate"));
        animesList.add(new Anime(3L, "Mashle"));
    }

    @Test
    @Order(1)
    @DisplayName("GET v1/animes returns a list with all animes when argument is null")
    void findAll_ReturnsAllAnimes_WhenArgumentIsNull() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);
        String response = fileUtils.readResourceFile("anime/get-anime-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(2)
    @DisplayName("GET v1/animes?name=Mashle returns a list with found object when name exists")
    void findAll_ReturnsFoundAnimeList_WhenNameIsFound() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);
        String response = fileUtils.readResourceFile("anime/get-anime-mashle-name-200.json");
        String name = "Mashle";

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes").param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(3)
    @DisplayName("GET v1/animes?name=x returns empty list when name is not found")
    void findAll_ReturnsEmptyList_WhenNameIsNotFound() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);
        String response = fileUtils.readResourceFile("anime/get-anime-x-name-200.json");
        String name = "x";

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes").param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(4)
    @DisplayName("GET v1/animes/1 returns an object with given id")
    void findById_ReturnsAnimeById_WhenSuccessful() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);
        String response = fileUtils.readResourceFile("anime/get-anime-by-id-200.json");
        Long id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(5)
    @DisplayName("GET v1/animes/99 throw ResponseStatusException 404 when anime is not found")
    void findByOd_ThrowsResponseStatusException_WhenAnimeIsNotFound() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);
        Long id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Anime not found"));
    }

    @Test
    @Order(6)
    @DisplayName("POST v1/animes creates a anime")
    void save_CreatesAnime_WhenSuccessful() throws Exception {
        String request = fileUtils.readResourceFile("anime/post-request-anime-200.json");
        String response = fileUtils.readResourceFile("anime/post-response-anime-201.json");

        Anime animeToSave = Anime.builder()
                .id(99L)
                .name("Overlord")
                .build();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(animeToSave);

        mockMvc.perform(MockMvcRequestBuilders
                                .post("/v1/animes")
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
        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);
        String request = fileUtils.readResourceFile("anime/put-request-anime-200.json");

        mockMvc.perform(MockMvcRequestBuilders
                                .put("/v1/animes")
                                .content(request)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @Order(8)
    @DisplayName("PUT v1/animes throw ResponseStatusException when anime is not found")
    void update_ThrowResponseStatusException_WhenAnimeIsNotFound() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);
        String request = fileUtils.readResourceFile("anime/put-request-anime-404.json");

        mockMvc.perform(MockMvcRequestBuilders
                                .put("/v1/animes")
                                .content(request)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Anime not found"));
    }

    @Test
    @Order(9)
    @DisplayName("DELETE v1/animes/1 removes a anime")
    void delete_RemovesAnime_WhenSuccessful() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);
        Long id = animesList.getFirst().getId();

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/animes/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @Order(10)
    @DisplayName("DELETE v1/animes/99 throw ResponseStatusException when anime is not found")
    void delete_ThrowResponseStatusException_WhenAnimeIsNotFound() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);
        Long id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/animes/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Anime not found"));
    }

}
