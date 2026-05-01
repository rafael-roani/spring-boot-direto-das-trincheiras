package dev.rafa.userservice.service;

import dev.rafa.userservice.domain.User;
import dev.rafa.userservice.domain.UserProfile;
import dev.rafa.userservice.repository.UserProfileRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {

  private final UserProfileRepository repository;

  public List<UserProfile> findAll() {
    return repository.retrieveAll();
  }

  public List<User> findAllUsersByProfileId(Long profileId) {
    return repository.findAllUsersByProfileId(profileId);
  }

}
