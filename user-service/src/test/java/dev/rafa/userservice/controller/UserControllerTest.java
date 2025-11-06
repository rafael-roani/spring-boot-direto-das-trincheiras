package dev.rafa.userservice.controller;

import dev.rafa.userservice.commons.FileUtils;
import dev.rafa.userservice.commons.UserUtils;
import dev.rafa.userservice.domain.User;
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
@WebMvcTest(controllers = UserController.class)
@ComponentScan(basePackages = {"dev.rafa.userservice"})
class UserControllerTest {

    private static final String URL = "/v1/users";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository repository;

    @MockBean
    private ProfileRepository profileRepository;

    private List<User> usersList;

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    private UserUtils userUtils;

    @BeforeEach
    void init() {
        usersList = userUtils.newUserList();
    }

    @Test
    @Order(1)
    @DisplayName("GET v1/users returns a list with all users when argument is null")
    void findAll_ReturnsAllUsers_WhenArgumentIsNull() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(usersList);
        String response = fileUtils.readResourceFile("user/get-user-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(2)
    @DisplayName("GET v1/users?name=Pedro returns a list with found object when name exists")
    void findAll_ReturnsFoundUserList_WhenNameIsFound() throws Exception {
        String response = fileUtils.readResourceFile("user/get-user-pedro-name-200.json");
        String name = "Pedro";
        User foundUser = usersList.stream()
                .filter(user -> user.getFirstName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);

        BDDMockito.when(repository.findByFirstNameIgnoreCase(name)).thenReturn(Collections.singletonList(foundUser));

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(3)
    @DisplayName("GET v1/users?name=x returns empty list when name is not found")
    void findAll_ReturnsEmptyList_WhenNameIsNotFound() throws Exception {
        String response = fileUtils.readResourceFile("user/get-user-x-name-200.json");
        String name = "x";

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(4)
    @DisplayName("GET v1/users/1 returns an object with given id")
    void findById_ReturnsUserById_WhenSuccessful() throws Exception {
        Long id = 1L;
        Optional<User> foundUser = usersList.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();

        BDDMockito.when(repository.findById(id)).thenReturn(foundUser);

        String response = fileUtils.readResourceFile("user/get-user-by-id-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(5)
    @DisplayName("GET v1/users/99 throw NotFound 404 when user is not found")
    void findByOd_ThrowsNotFound_WhenUserIsNotFound() throws Exception {
        String response = fileUtils.readResourceFile("user/get-user-by-id-404.json");
        Long id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(6)
    @DisplayName("POST v1/users creates a user")
    void save_CreatesUser_WhenSuccessful() throws Exception {
        String request = fileUtils.readResourceFile("user/post-request-user-200.json");
        String response = fileUtils.readResourceFile("user/post-response-user-201.json");

        User userToSave = userUtils.newUserToSave();
        userToSave.setId(1L);

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(userToSave);

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
    @DisplayName("PUT v1/users updates a user")
    void update_UpdatesUser_WhenSuccessful() throws Exception {
        Long id = usersList.getFirst().getId();
        Optional<User> foundUser = usersList.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();

        BDDMockito.when(repository.findById(id)).thenReturn(foundUser);

        String request = fileUtils.readResourceFile("user/put-request-user-200.json");

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
    @DisplayName("PUT v1/users throw NotFound when user is not found")
    void update_ThrowNotFound_WhenUserIsNotFound() throws Exception {
        String request = fileUtils.readResourceFile("user/put-request-user-404.json");
        String response = fileUtils.readResourceFile("user/put-user-by-id-404.json");

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
    @DisplayName("DELETE v1/users/1 removes a user")
    void delete_RemovesUser_WhenSuccessful() throws Exception {
        Long id = usersList.getFirst().getId();
        Optional<User> foundUser = usersList.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();

        BDDMockito.when(repository.findById(id)).thenReturn(foundUser);

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @Order(10)
    @DisplayName("DELETE v1/users/99 throw NotFound when user is not found")
    void delete_ThrowNotFound_WhenUserIsNotFound() throws Exception {
        String response = fileUtils.readResourceFile("user/delete-user-by-id-404.json");
        Long id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Order(11)
    @ParameterizedTest
    @MethodSource("postUserBadRequestSource")
    @DisplayName("POST v1/users returns bad request when fields are invalid")
    void save_ReturnsBadRequest_WhenFieldsAreEmpty(String fileName, List<String> errors) throws Exception {
        String request = fileUtils.readResourceFile("user/%s".formatted(fileName));

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

        Assertions.assertThat(resolvedException).isNotNull();

        Assertions.assertThat(resolvedException.getMessage())
                .contains(errors);
    }

    @Order(12)
    @ParameterizedTest
    @MethodSource("putUserBadRequestSource")
    @DisplayName("PUT v1/users returns bad request when fields are invalid")
    void update_ReturnsBadRequest_WhenFieldsAreEmpty(String fileName, List<String> errors) throws Exception {
        String request = fileUtils.readResourceFile("user/%s".formatted(fileName));

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

    private static Stream<Arguments> postUserBadRequestSource() {
        List<String> allRequiredErrors = allRequiredErrors();
        List<String> emailInvalidErrors = invalidEmailErrors();

        return Stream.of(
                Arguments.of("post-request-user-empty-fields-400.json", allRequiredErrors),
                Arguments.of("post-request-user-blank-fields-400.json", allRequiredErrors),
                Arguments.of("post-request-user-invalid-email-400.json", emailInvalidErrors)
        );
    }

    private static Stream<Arguments> putUserBadRequestSource() {
        List<String> allRequiredErrors = allRequiredErrors();
        List<String> emailInvalidErrors = invalidEmailErrors();

        allRequiredErrors.add("The field 'id' cannot be null");

        return Stream.of(
                Arguments.of("put-request-user-empty-fields-400.json", allRequiredErrors),
                Arguments.of("put-request-user-blank-fields-400.json", allRequiredErrors),
                Arguments.of("put-request-user-invalid-email-400.json", emailInvalidErrors)
        );
    }

    private static List<String> allRequiredErrors() {
        String firstNameRequiredError = "The field 'firstName' is required";
        String lastNameRequiredError = "The field 'lastName' is required";
        String emailRequiredError = "The field 'email' is required";

        return new ArrayList<>(List.of(firstNameRequiredError, lastNameRequiredError, emailRequiredError));
    }

    private static List<String> invalidEmailErrors() {
        String emailInvalidError = "The e-mail is not valid";
        return Collections.singletonList(emailInvalidError);
    }

}
