package dev.rafa.userservice.service;

import dev.rafa.commonscore.exception.EmailAlreadyExistsException;
import dev.rafa.userservice.commons.UserUtils;
import dev.rafa.userservice.domain.User;
import dev.rafa.userservice.repository.UserRepository;
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
    private UserRepository repository;

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
        Assertions.assertThat(users)
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

        BDDMockito.when(repository.findByFirstNameIgnoreCase(user.getFirstName()))
                .thenReturn(expectedUsers);

        List<User> usersFound = service.findAll(user.getFirstName());
        Assertions.assertThat(usersFound)
                .isNotNull()
                .isNotEmpty()
                .containsAll(expectedUsers);
    }

    @Test
    @Order(3)
    @DisplayName("findAll returns empty list when name is not found")
    void findAll_ReturnsEmptyList_WhenNameIsNotFound() {
        String name = "not-found";
        BDDMockito.when(repository.findByFirstNameIgnoreCase(name)).thenReturn(Collections.emptyList());

        List<User> users = service.findAll(name);
        Assertions.assertThat(users)
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

        Assertions.assertThat(user).isEqualTo(expectedUser);
    }

    @Test
    @Order(5)
    @DisplayName("findById throw ResponseStatusException when user is not found")
    void findByOd_ThrowsResponseStatusException_WhenUserIsNotFound() {
        User expectedUser = userList.getFirst();
        BDDMockito.when(repository.findById(expectedUser.getId()))
                .thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.findByIdOrThrowNotFound(expectedUser.getId()))
                .isInstanceOf(ResponseStatusException.class);

    }

    @Test
    @Order(6)
    @DisplayName("save creates a user")
    void save_CreatesUser_WhenSuccessful() {
        User userSaved = userUtils.newUserSaved();

        BDDMockito.when(repository.save(userSaved)).thenReturn(userSaved);
        BDDMockito.when(repository.findByEmailIgnoreCase(userSaved.getEmail())).thenReturn(Optional.empty());

        User savedUser = service.save(userSaved);
        Assertions.assertThat(savedUser)
                .isNotNull()
                .isEqualTo(userSaved)
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

        Assertions.assertThatNoException().isThrownBy(() -> service.delete(userToDelete.getId()));
    }

    @Test
    @Order(8)
    @DisplayName("delete throw ResponseStatusException when user is not found")
    void delete_ThrowResponseStatusException_WhenUserIsNotFound() {
        User userToDelete = userList.getFirst();

        BDDMockito.when(repository.findById(userToDelete.getId()))
                .thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.delete(userToDelete.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @Order(9)
    @DisplayName("update updates a user")
    void update_UpdatesUser_WhenSuccessful() {
        User userToUpdate = userList.getFirst().withFirstName("Rafael");

        String email = userToUpdate.getEmail();
        Long id = userToUpdate.getId();

        BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(userToUpdate));
        BDDMockito.when(repository.findByEmailIgnoreCaseAndIdNot(email, id)).thenReturn(Optional.empty());
        BDDMockito.when(repository.save(userToUpdate)).thenReturn(userToUpdate);

        Assertions.assertThatNoException().isThrownBy(() -> service.update(userToUpdate));
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

    @Test
    @Order(11)
    @DisplayName("update throw EmailAlreadyExistsException when email belongs to another user")
    void update_ThrowEmailAlreadyExistsException_WhenEmailBelongsToAnotherUser() {
        User savedUser = userList.getLast();
        User userToUpdate = userList.getFirst().withEmail(savedUser.getEmail());

        String email = userToUpdate.getEmail();
        Long id = userToUpdate.getId();

        BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(userToUpdate));
        BDDMockito.when(repository.findByEmailIgnoreCaseAndIdNot(email, id)).thenReturn(Optional.of(savedUser));

        Assertions.assertThatException()
                .isThrownBy(() -> service.update(userToUpdate))
                .isInstanceOf(EmailAlreadyExistsException.class);
    }

    @Test
    @Order(12)
    @DisplayName("save throw EmailAlreadyExistsException when email exists")
    void save_ThrowEmailAlreadyExistsException_WhenEmailExists() {
        User savedUser = userList.getLast();
        User userToSave = userUtils.newUserToSave().withEmail(savedUser.getEmail());

        String email = userToSave.getEmail();

        BDDMockito.when(repository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(savedUser));

        Assertions.assertThatException()
                .isThrownBy(() -> service.save(userToSave))
                .isInstanceOf(EmailAlreadyExistsException.class);
    }

}