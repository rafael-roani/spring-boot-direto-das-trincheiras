package dev.rafa.userservice.service;

import dev.rafa.commonscore.exception.NotFoundException;
import dev.rafa.userservice.domain.User;
import dev.rafa.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public List<User> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByFirstNameIgnoreCase(name);
    }

    public void delete(Long id) {
        User userToDelete = findByIdOrThrowNotFound(id);
        repository.delete(userToDelete);
    }

    public void update(User userToUpdate) {
        assertUserExists(userToUpdate.getId());
        assertEmailDoesNotExist(userToUpdate.getEmail(), userToUpdate.getId());
        repository.save(userToUpdate);
    }

    public void assertUserExists(Long id) {
        findByIdOrThrowNotFound(id);
    }

    public User findByIdOrThrowNotFound(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public User save(User user) {
        assertEmailDoesNotExist(user.getEmail());
        return repository.save(user);
    }

    public void assertEmailDoesNotExist(String email) {
        repository.findByEmailIgnoreCase(email)
                .ifPresent(this::throwEmailExistsException);
    }

    public void assertEmailDoesNotExist(String email, Long id) {
        repository.findByEmailIgnoreCaseAndIdNot(email, id)
                .ifPresent(this::throwEmailExistsException);
    }

    private void throwEmailExistsException(User user) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-mail %s already exists".formatted(user.getEmail()));
    }

}
