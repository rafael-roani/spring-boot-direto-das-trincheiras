package dev.rafa.userservice.service;

import dev.rafa.userservice.domain.Profile;
import dev.rafa.userservice.repository.ProfileRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
