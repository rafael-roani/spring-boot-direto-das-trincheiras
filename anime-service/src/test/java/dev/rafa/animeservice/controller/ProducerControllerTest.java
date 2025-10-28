package dev.rafa.animeservice.controller;

import dev.rafa.animeservice.commons.FileUtils;
import dev.rafa.animeservice.commons.ProducerUtils;
import dev.rafa.animeservice.domain.Producer;
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

@WebMvcTest(controllers = ProducerController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = {"dev.rafa.animeservice"})
class ProducerControllerTest {

    private static final String URL = "/v1/producers";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnimeRepository animeRepository;

    @MockBean
    private ProducerRepository repository;

    private List<Producer> producerList;

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    private ProducerUtils producerUtils;

    @BeforeEach
    void init() {
        producerList = producerUtils.newProducerList();
    }

    @Test
    @Order(1)
    @DisplayName("GET v1/producers returns a list with all producers when argument is null")
    void findAll_ReturnsAllProducers_WhenArgumentIsNull() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(producerList);
        String response = fileUtils.readResourceFile("producer/get-producer-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(2)
    @DisplayName("GET v1/producers?name=Ufotable returns a list with found object when name exists")
    void findAll_ReturnsFoundProducerList_WhenNameIsFound() throws Exception {
        String response = fileUtils.readResourceFile("producer/get-producer-ufotable-name-200.json");
        String name = "Ufotable";
        Producer producer = producerList.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElse(null);
        BDDMockito.when(repository.findByName(name)).thenReturn(Collections.singletonList(producer));

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(3)
    @DisplayName("GET v1/producers?name=x returns empty list when name is not found")
    void findAll_ReturnsEmptyList_WhenNameIsNotFound() throws Exception {
        String response = fileUtils.readResourceFile("producer/get-producer-x-name-200.json");
        String name = "x";

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(4)
    @DisplayName("GET v1/producers/1 returns an object with given id")
    void findById_ReturnsProducerById_WhenSuccessful() throws Exception {
        String response = fileUtils.readResourceFile("producer/get-producer-by-id-200.json");
        Long id = 1L;
        Optional<Producer> foundProducer = producerList.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
        BDDMockito.when(repository.findById(id)).thenReturn(foundProducer);

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(5)
    @DisplayName("GET v1/producers/99 throw NotFound 404 when producer is not found")
    void findByOd_ThrowsNotFound_WhenProducerIsNotFound() throws Exception {
        String response = fileUtils.readResourceFile("producer/get-producer-by-id-404.json");
        Long id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(6)
    @DisplayName("POST v1/producers creates a producer")
    void save_CreatesProducer_WhenSuccessful() throws Exception {
        String request = fileUtils.readResourceFile("producer/post-request-producer-200.json");
        String response = fileUtils.readResourceFile("producer/post-response-producer-201.json");

        Producer producerToSave = producerUtils.newProducerToSave();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(producerToSave);

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
    @DisplayName("PUT v1/producers updates a producer")
    void update_UpdatesProducer_WhenSuccessful() throws Exception {
        String request = fileUtils.readResourceFile("producer/put-request-producer-200.json");
        Long id = 1L;
        Optional<Producer> foundProducer = producerList.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
        BDDMockito.when(repository.findById(id)).thenReturn(foundProducer);

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
    @DisplayName("PUT v1/producers throw NotFound when producer is not found")
    void update_ThrowNotFound_WhenProducerIsNotFound() throws Exception {
        String request = fileUtils.readResourceFile("producer/put-request-producer-404.json");
        String response = fileUtils.readResourceFile("producer/put-producer-by-id-404.json");

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
    @DisplayName("DELETE v1/producers/1 removes a producer")
    void delete_RemovesProducer_WhenSuccessful() throws Exception {
        Long id = producerList.getFirst().getId();
        Optional<Producer> foundProducer = producerList.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
        BDDMockito.when(repository.findById(id)).thenReturn(foundProducer);

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @Order(10)
    @DisplayName("DELETE v1/producers/99 throw NotFound when producer is not found")
    void delete_ThrowNotFound_WhenProducerIsNotFound() throws Exception {
        String response = fileUtils.readResourceFile("producer/delete-producer-by-id-404.json");
        Long id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Order(11)
    @ParameterizedTest
    @MethodSource("postProducerBadRequestSource")
    @DisplayName("POST v1/producers returns bad request when fields are invalid")
    void save_ReturnsBadRequest_WhenFieldsAreEmpty(String fileName, List<String> errors) throws Exception {
        String request = fileUtils.readResourceFile("producer/%s".formatted(fileName));

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
    @MethodSource("putProducerBadRequestSource")
    @DisplayName("PUT v1/producers returns bad request when fields are invalid")
    void update_ReturnsBadRequest_WhenFieldsAreEmpty(String fileName, List<String> errors) throws Exception {
        String request = fileUtils.readResourceFile("producer/%s".formatted(fileName));

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

        org.assertj.core.api.Assertions.assertThat(resolvedException).isNotNull();

        Assertions.assertThat(resolvedException.getMessage())
                .contains(errors);
    }

    private static Stream<Arguments> postProducerBadRequestSource() {
        List<String> allRequiredErrors = allRequiredErrors();

        return Stream.of(
                Arguments.of("post-request-producer-empty-fields-400.json", allRequiredErrors),
                Arguments.of("post-request-producer-blank-fields-400.json", allRequiredErrors)
        );
    }

    private static Stream<Arguments> putProducerBadRequestSource() {
        List<String> allRequiredErrors = allRequiredErrors();

        allRequiredErrors.add("The field 'id' cannot be null");

        return Stream.of(
                Arguments.of("put-request-producer-empty-fields-400.json", allRequiredErrors),
                Arguments.of("put-request-producer-blank-fields-400.json", allRequiredErrors)
        );
    }

    private static List<String> allRequiredErrors() {
        String nameRequiredError = "The field 'name' is required";

        return new ArrayList<>(List.of(nameRequiredError));
    }

}
