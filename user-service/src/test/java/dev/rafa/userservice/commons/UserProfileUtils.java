package dev.rafa.userservice.commons;

import dev.rafa.userservice.domain.UserProfile;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserProfileUtils {

  private final UserUtils userUtils;

  private final ProfileUtils profileUtils;

  public List<UserProfile> newUserProfileList() {
    UserProfile regularUserProfile = newUserProfileSaved();
    return new ArrayList<>(List.of(regularUserProfile));
  }

  public UserProfile newUserProfileToSave() {
    return UserProfile.builder()
        .user(userUtils.newUserSaved())
        .profile(profileUtils.newProfileSaved())
        .build();
  }

  public UserProfile newUserProfileSaved() {
    return UserProfile.builder()
        .id(1L)
        .user(userUtils.newUserSaved())
        .profile(profileUtils.newProfileSaved())
        .build();
  }

}
