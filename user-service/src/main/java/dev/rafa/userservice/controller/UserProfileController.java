package dev.rafa.userservice.controller;

import dev.rafa.userservice.domain.UserProfile;
import dev.rafa.userservice.mapper.UserProfileMapper;
import dev.rafa.userservice.response.UserProfileGetResponse;
import dev.rafa.userservice.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/user-profiles")
public class UserProfileController {

    private final UserProfileService service;

    private final UserProfileMapper mapper;

    @GetMapping
    public ResponseEntity<List<UserProfileGetResponse>> findAll() {
        log.debug("Request received to list all user profiles");

        List<UserProfile> userProfiles = service.findAll();

        List<UserProfileGetResponse> userProfileGetResponses = mapper.toUserProfileGetResponse(userProfiles);

        return ResponseEntity.ok(userProfileGetResponses);
    }

}
