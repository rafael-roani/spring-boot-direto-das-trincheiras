package dev.rafa.userservice.service;

import dev.rafa.commonscore.exception.NotFoundException;
import dev.rafa.userservice.domain.User;
import dev.rafa.userservice.repository.UserHardCodedRepository;
import dev.rafa.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserHardCodedRepository repository;

    private final UserRepository userRepository;

    public List<User> findAll(String name) {
        return name == null ? userRepository.findAll() : repository.findByName(name);
    }

    public void delete(Long id) {
        User userToDelete = findByIdOrThrowNotFound(id);
        repository.delete(userToDelete);
    }

    public void update(User userToUpdate) {
        User user = findByIdOrThrowNotFound(userToUpdate.getId());
        repository.update(userToUpdate);
    }

    public User findByIdOrThrowNotFound(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public User save(User user) {
        return repository.save(user);
    }

}
