package dev.rafa.userservice.service;

import dev.rafa.userservice.domain.Profile;
import dev.rafa.userservice.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository repository;

    public List<Profile> findAll() {
        return repository.findAll();
    }

    public Profile save(Profile profile) {
        return repository.save(profile);
    }

}
