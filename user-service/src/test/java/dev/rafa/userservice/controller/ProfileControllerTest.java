package dev.rafa.userservice.controller;

import dev.rafa.userservice.commons.FileUtils;
import dev.rafa.userservice.commons.ProfileUtils;
import dev.rafa.userservice.domain.Profile;
import dev.rafa.userservice.repository.ProfileRepository;
import dev.rafa.userservice.repository.UserRepository;
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
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebMvcTest(controllers = ProfileController.class)
@Import({FileUtils.class, ProfileUtils.class})
class ProfileControllerTest {

    private static final String URL = "/v1/profiles";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfileRepository repository;

    @MockBean
    private UserRepository userRepository;

    private List<Profile> profilesList;

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    private ProfileUtils profileUtils;

    @BeforeEach
    void init() {
        profilesList = profileUtils.newProfileList();
    }

    @Test
    @Order(1)
    @DisplayName("GET v1/profiles returns a list with all profiles")
    void findAll_ReturnsAllProfiles_WhenSuccessful() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(profilesList);
        String response = fileUtils.readResourceFile("profile/get-profiles-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(2)
    @DisplayName("GET v1/profiles returns empty list when nothing is not found")
    void findAll_ReturnsEmptyList_WhenNothingIsNotFound() throws Exception {
        String response = fileUtils.readResourceFile("profile/get-profiles-empty-list-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(3)
    @DisplayName("POST v1/profiles creates a profile")
    void save_CreatesProfile_WhenSuccessful() throws Exception {
        String request = fileUtils.readResourceFile("profile/post-request-profile-200.json");
        String response = fileUtils.readResourceFile("profile/post-response-profile-201.json");

        Profile profileSaved = profileUtils.newProfileSaved();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(profileSaved);

        mockMvc.perform(MockMvcRequestBuilders
                                .post(URL)
                                .content(request)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Order(11)
    @ParameterizedTest
    @MethodSource("postProfileBadRequestSource")
    @DisplayName("POST v1/profiles returns bad request when fields are invalid")
    void save_ReturnsBadRequest_WhenFieldsAreEmpty(String fileName, List<String> errors) throws Exception {
        String request = fileUtils.readResourceFile("profile/%s".formatted(fileName));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                                                      .post(URL)
                                                      .content(request)
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

    private static Stream<Arguments> postProfileBadRequestSource() {
        List<String> allRequiredErrors = allRequiredErrors();

        return Stream.of(
                Arguments.of("post-request-profile-empty-fields-400.json", allRequiredErrors),
                Arguments.of("post-request-profile-blank-fields-400.json", allRequiredErrors)
        );
    }

    private static List<String> allRequiredErrors() {
        String nameRequiredError = "The field 'name' is required";
        String descriptionRequiredError = "The field 'description' is required";

        return new ArrayList<>(List.of(nameRequiredError, descriptionRequiredError));
    }

}
