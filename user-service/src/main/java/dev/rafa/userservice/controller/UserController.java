package dev.rafa.userservice.controller;

import dev.rafa.commonscore.exception.ApiError;
import dev.rafa.commonscore.exception.DefaultErrorMessage;
import dev.rafa.userservice.domain.User;
import dev.rafa.userservice.mapper.UserMapper;
import dev.rafa.userservice.request.UserPostRequest;
import dev.rafa.userservice.request.UserPutRequest;
import dev.rafa.userservice.response.UserGetResponse;
import dev.rafa.userservice.response.UserPostResponse;
import dev.rafa.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@EnableMethodSecurity
@RequiredArgsConstructor
@RequestMapping("v1/users")
@Tag(name = "User API", description = "User related endpoints")
@SecurityRequirement(name = "basicAuth")
public class UserController {

  private final UserMapper mapper;

  private final UserService service;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Get all users",
      description = "Get all users available in the system, optionally filtered by name",
      responses = {
          @ApiResponse(
              description = "List of users",
              responseCode = "200",
              content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                  array = @ArraySchema(schema = @Schema(implementation = UserGetResponse.class))
              )
          ),
      })
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<List<UserGetResponse>> findAll(@RequestParam(required = false) String name) {
    log.debug("Request received to list all users, param name '{}'", name);

    List<User> users = service.findAll(name);

    List<UserGetResponse> response = mapper.toUserGetResponseList(users);

    return ResponseEntity.ok(response);
  }

  @GetMapping("{id}")
  @Operation(
      summary = "Get user by id",
      responses = {
          @ApiResponse(
              description = "Get a user by its id",
              responseCode = "200",
              content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserGetResponse.class))
          ),
          @ApiResponse(
              description = "User Not Found",
              responseCode = "404",
              content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DefaultErrorMessage.class))
          )
      })
  public ResponseEntity<UserGetResponse> findById(@PathVariable Long id) {
    log.debug("Request to find user by id: {}", id);

    User user = service.findByIdOrThrowNotFound(id);

    UserGetResponse response = mapper.toUserGetResponse(user);

    return ResponseEntity.ok(response);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(
      summary = "Create a new user",
      responses = {
          @ApiResponse(
              description = "Save user in the database",
              responseCode = "201",
              content = @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = UserPostResponse.class)
              )
          ),
          @ApiResponse(
              description = "Bad Request",
              responseCode = "400",
              content = @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ApiError.class)
              )
          )
      })
  public ResponseEntity<UserPostResponse> save(@RequestBody @Valid UserPostRequest userPostRequest) {
    log.debug("Request to save user: {}", userPostRequest);

    User userToSave = mapper.toUser(userPostRequest);

    User savedUser = service.save(userToSave);

    UserPostResponse response = mapper.toUserPostResponse(savedUser);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @DeleteMapping("{id}")
  @PreAuthorize("hasAuthority('ADMIN')")
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
