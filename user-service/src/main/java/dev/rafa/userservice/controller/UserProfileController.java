package dev.rafa.userservice.controller;

import dev.rafa.userservice.domain.UserProfile;
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

    @GetMapping
    public ResponseEntity<List<UserProfile>> findAll() {
        log.debug("Request received to list all user profiles");

        List<UserProfile> userProfiles = service.findAll();

        return ResponseEntity.ok(userProfiles);
    }

}
