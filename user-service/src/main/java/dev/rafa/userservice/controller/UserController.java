package dev.rafa.userservice.controller;

import dev.rafa.userservice.domain.User;
import dev.rafa.userservice.mapper.UserMapper;
import dev.rafa.userservice.request.UserPostRequest;
import dev.rafa.userservice.request.UserPutRequest;
import dev.rafa.userservice.response.UserGetResponse;
import dev.rafa.userservice.response.UserPostResponse;
import dev.rafa.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/users")
public class UserController {

    private final UserMapper mapper;

    private final UserService service;

    @GetMapping
    public ResponseEntity<List<UserGetResponse>> findAll(@RequestParam(required = false) String name) {
        log.debug("Request received to list all users, param name '{}'", name);

        List<User> users = service.findAll(name);

        List<UserGetResponse> response = mapper.toUserGetResponseList(users);

        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserGetResponse> findById(@PathVariable Long id) {
        log.debug("Request to find user by id: {}", id);

        User user = service.findByIdOrThrowNotFound(id);

        UserGetResponse response = mapper.toUserGetResponse(user);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<UserPostResponse> save(@RequestBody @Valid UserPostRequest userPostRequest) {
        log.debug("Request to save user: {}", userPostRequest);

        User userToSave = mapper.toUser(userPostRequest);

        User savedUser = service.save(userToSave);

        UserPostResponse response = mapper.toUserPostResponse(savedUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("Deleting user by id: {}", id);

        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid UserPutRequest request) {
        log.debug("Updating user: {}", request);

        User userToUpdate = mapper.toUser(request);

        service.update(userToUpdate);

        return ResponseEntity.noContent().build();
    }

}
