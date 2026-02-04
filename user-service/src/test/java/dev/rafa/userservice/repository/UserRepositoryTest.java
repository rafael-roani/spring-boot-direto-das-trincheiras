package dev.rafa.userservice.repository;

import dev.rafa.userservice.commons.UserUtils;
import dev.rafa.userservice.config.TestcontainersConfiguration;
import dev.rafa.userservice.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Import({UserUtils.class, TestcontainersConfiguration.class})
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserUtils userUtils;

    @Test
    @Order(1)
    @DisplayName("Save creates an user")
    void save_CreatesUser_WhenSuccessful() {
        User userToSave = userUtils.newUserToSave();
        User savedUser = repository.save(userToSave);

        Assertions.assertThat(savedUser).hasNoNullFieldsOrProperties();
        Assertions.assertThat(savedUser.getId()).isPositive();
    }

    @Test
    @Order(2)
    @Sql(scripts = {"/sql/init_one_user.sql"})
    @DisplayName("findAll returns a list with all users")
    void findAll_ReturnsAllUsers_WhenSuccessful() {
        Iterable<User> users = repository.findAll();
        Assertions.assertThat(users).isNotEmpty();
    }

}
