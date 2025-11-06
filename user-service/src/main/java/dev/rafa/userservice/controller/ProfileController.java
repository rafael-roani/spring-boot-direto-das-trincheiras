package dev.rafa.userservice.controller;

import dev.rafa.userservice.domain.Profile;
import dev.rafa.userservice.mapper.ProfileMapper;
import dev.rafa.userservice.request.ProfilePostRequest;
import dev.rafa.userservice.response.ProfileGetResponse;
import dev.rafa.userservice.response.ProfilePostResponse;
import dev.rafa.userservice.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/profiles")
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

        return ResponseEntity.ok(response);
    }

}
