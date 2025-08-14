package dev.rafa.userservice.service;

import dev.rafa.userservice.commons.UserUtils;
import dev.rafa.userservice.domain.User;
import dev.rafa.userservice.repository.UserHardCodedRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Mock
    private UserHardCodedRepository repository;

    private List<User> userList;

    @InjectMocks
    private UserUtils userUtils;

    @BeforeEach
    void init() {
        userList = userUtils.newUserList();
    }

    @Test
    @Order(1)
    @DisplayName("findAll returns a list with all users when argument is null")
    void findAll_ReturnsAllUsers_WhenArgumentIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(userList);

        List<User> users = service.findAll(null);
        org.assertj.core.api.Assertions.assertThat(users)
                .isNotNull()
                .hasSize(userList.size())
                .containsAll(this.userList);
    }

    @Test
    @Order(2)
    @DisplayName("findAll returns list with found object when name is exists")
    void findAll_ReturnsFoundUserInList_WhenNameIsFound() {
        User user = userList.getFirst();
        List<User> expectedUsers = Collections.singletonList(user);

        BDDMockito.when(repository.findByName(user.getFirstName()))
                .thenReturn(expectedUsers);

        List<User> usersFound = service.findAll(user.getFirstName());
        org.assertj.core.api.Assertions.assertThat(usersFound)
                .isNotNull()
                .isNotEmpty()
                .containsAll(expectedUsers);
    }

    @Test
    @Order(3)
    @DisplayName("findAll returns empty list when name is not found")
    void findAll_ReturnsEmptyList_WhenNameIsNotFound() {
        String name = "not-found";
        BDDMockito.when(repository.findByName(name)).thenReturn(Collections.emptyList());

        List<User> users = service.findAll(name);
        org.assertj.core.api.Assertions.assertThat(users)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @Order(4)
    @DisplayName("findById returns an object with given id")
    void findById_ReturnsUserById_WhenSuccessful() {
        User expectedUser = userList.getFirst();
        BDDMockito.when(repository.findById(expectedUser.getId()))
                .thenReturn(Optional.of(expectedUser));

        User user = service.findByIdOrThrowNotFound(expectedUser.getId());

        org.assertj.core.api.Assertions.assertThat(user).isEqualTo(expectedUser);
    }

    @Test
    @Order(5)
    @DisplayName("findById throw ResponseStatusException when user is not found")
    void findByOd_ThrowsResponseStatusException_WhenUserIsNotFound() {
        User expectedUser = userList.getFirst();
        BDDMockito.when(repository.findById(expectedUser.getId()))
                .thenReturn(Optional.empty());

        org.assertj.core.api.Assertions.assertThatException()
                .isThrownBy(() -> service.findByIdOrThrowNotFound(expectedUser.getId()))
                .isInstanceOf(ResponseStatusException.class);

    }

    @Test
    @Order(6)
    @DisplayName("save creates a user")
    void save_CreatesUser_WhenSuccessful() {
        User userToSave = userUtils.newUserToSave();

        BDDMockito.when(repository.save(userToSave)).thenReturn(userToSave);

        User savedUser = service.save(userToSave);
        org.assertj.core.api.Assertions.assertThat(savedUser)
                .isNotNull()
                .isEqualTo(userToSave)
                .hasNoNullFieldsOrProperties();
    }

    @Test
    @Order(7)
    @DisplayName("delete removes a user")
    void delete_RemovesUser_WhenSuccessful() {
        User userToDelete = userList.getFirst();
        BDDMockito.when(repository.findById(userToDelete.getId()))
                .thenReturn(Optional.of(userToDelete));
        BDDMockito.doNothing().when(repository).delete(userToDelete);

        org.assertj.core.api.Assertions.assertThatNoException().isThrownBy(() -> service.delete(userToDelete.getId()));
    }

    @Test
    @Order(8)
    @DisplayName("delete throw ResponseStatusException when user is not found")
    void delete_ThrowResponseStatusException_WhenUserIsNotFound() {
        User userToDelete = userList.getFirst();

        BDDMockito.when(repository.findById(userToDelete.getId()))
                .thenReturn(Optional.empty());

        org.assertj.core.api.Assertions.assertThatException()
                .isThrownBy(() -> service.delete(userToDelete.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @Order(9)
    @DisplayName("update updates a user")
    void update_UpdatesUser_WhenSuccessful() {
        User userToUpdate = userList.getFirst();
        userToUpdate.setFirstName("Rafael");

        BDDMockito.when(repository.findById(userToUpdate.getId()))
                .thenReturn(Optional.of(userToUpdate));
        BDDMockito.doNothing().when(repository).update(userToUpdate);

        org.assertj.core.api.Assertions.assertThatNoException().isThrownBy(() -> service.update(userToUpdate));
    }

    @Test
    @Order(10)
    @DisplayName("update throw ResponseStatusException when user is not found")
    void update_ThrowResponseStatusException_WhenUserIsNotFound() {
        User userToUpdate = userList.getFirst();
        userToUpdate.setFirstName("Rafael");

        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.update(userToUpdate))
                .isInstanceOf(ResponseStatusException.class);
    }

}