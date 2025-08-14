package dev.rafa.userservice.repository;

import dev.rafa.userservice.commons.UserUtils;
import dev.rafa.userservice.domain.User;
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
class UserHardCodedRepositoryTest {

    @InjectMocks
    private UserHardCodedRepository repository;

    @Mock
    private UserData userData;

    private List<User> userList;

    @InjectMocks
    private UserUtils userUtils;

    @BeforeEach
    void init() {
        userList = userUtils.newUserList();
    }

    @Test
    @Order(1)
    @DisplayName("findAll returns a list with all users")
    void findAll_ReturnsAllUsers_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        List<User> users = repository.findAll();
        Assertions.assertThat(users)
                .isNotNull()
                .hasSize(userList.size())
                .containsAll(this.userList);
    }

    @Test
    @Order(2)
    @DisplayName("findById returns an user with given id")
    void findAll_ReturnsUserById_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        User userExpected = userList.getFirst();
        Optional<User> user = repository.findById(userExpected.getId());

        Assertions.assertThat(user)
                .isPresent()
                .contains(userExpected);
    }

    @Test
    @Order(3)
    @DisplayName("findByName returns empty list when name is null")
    void findByName_ReturnsEmptyList_WhenNameIsNull() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        List<User> users = repository.findByName(null);
        Assertions.assertThat(users)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @Order(4)
    @DisplayName("findByName returns list with found object when name is exists")
    void findByName_ReturnsFoundUserInList_WhenNameIsFound() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        User expectedUser = userList.getFirst();
        List<User> users = repository.findByName(expectedUser.getFirstName());
        Assertions.assertThat(users)
                .isNotNull()
                .isNotEmpty()
                .contains(expectedUser);
    }

    @Test
    @Order(5)
    @DisplayName("save creates a user")
    void save_CreatesUser_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        User userToSave = userUtils.newUserToSave();

        User user = repository.save(userToSave);
        Assertions.assertThat(user)
                .isNotNull()
                .isEqualTo(userToSave)
                .hasNoNullFieldsOrProperties();

        Optional<User> userSaved = repository.findById(userToSave.getId());
        Assertions.assertThat(userSaved)
                .isPresent()
                .contains(userToSave);
    }

    @Test
    @Order(6)
    @DisplayName("delete removes a user")
    void delete_RemovesUser_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        User userToDelete = userList.getFirst();
        repository.delete(userToDelete);

        List<User> users = repository.findAll();
        Assertions.assertThat(users).doesNotContain(userToDelete);
    }

    @Test
    @Order(7)
    @DisplayName("update updates a user")
    void update_UpdatesUser_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        User userToUpdate = userList.getFirst();
        userToUpdate.setFirstName("Rafa");

        repository.update(userToUpdate);

        Assertions.assertThat(this.userList).contains(userToUpdate);

        Optional<User> userUpdated = repository.findById(userToUpdate.getId());

        Assertions.assertThat(userUpdated).isPresent();
        Assertions.assertThat(userUpdated.get().getFirstName())
                .isEqualTo(userToUpdate.getFirstName());
    }

}
