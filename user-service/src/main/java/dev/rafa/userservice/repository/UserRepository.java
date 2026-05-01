package dev.rafa.userservice.repository;

import dev.rafa.userservice.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  List<User> findByFirstNameIgnoreCase(String firstName);

  Optional<User> findByEmailIgnoreCase(String email);

  Optional<User> findByEmailIgnoreCaseAndIdNot(String email, Long id);

}
