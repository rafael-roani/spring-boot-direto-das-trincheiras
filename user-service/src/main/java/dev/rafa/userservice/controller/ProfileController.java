package dev.rafa.userservice.controller;

import dev.rafa.userservice.domain.Profile;
import dev.rafa.userservice.mapper.ProfileMapper;
import dev.rafa.userservice.request.ProfilePostRequest;
import dev.rafa.userservice.response.ProfileGetResponse;
import dev.rafa.userservice.response.ProfilePostResponse;
import dev.rafa.userservice.service.ProfileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/profiles")
@SecurityRequirement(name = "basicAuth")
public class ProfileController {

  private final ProfileService service;

  private final ProfileMapper mapper;

  @GetMapping
  public ResponseEntity<List<ProfileGetResponse>> findAll() {
    log.debug("Request received to list all profiles");

    List<Profile> profiles = service.findAll();

    List<ProfileGetResponse> response = mapper.toProfileGetResponse(profiles);

    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<ProfilePostResponse> save(@RequestBody @Valid ProfilePostRequest profilePostRequest) {
    log.debug("Request to save profile: {}", profilePostRequest);

    Profile profile = mapper.toProfile(profilePostRequest);

    Profile savedProfile = service.save(profile);

    ProfilePostResponse response = mapper.toProfilePostResponse(savedProfile);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

}
