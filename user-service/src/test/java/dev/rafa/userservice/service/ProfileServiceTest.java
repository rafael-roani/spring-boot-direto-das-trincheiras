package dev.rafa.userservice.service;

import dev.rafa.userservice.commons.ProfileUtils;
import dev.rafa.userservice.domain.Profile;
import dev.rafa.userservice.repository.ProfileRepository;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProfileServiceTest {

  @InjectMocks
  private ProfileService service;

  @Mock
  private ProfileRepository repository;

  private List<Profile> profileList;

  @InjectMocks
  private ProfileUtils profileUtils;

  @BeforeEach
  void init() {
    profileList = profileUtils.newProfileList();
  }

  @Test
  @Order(1)
  @DisplayName("findAll returns a list with all profiles")
  void findAll_ReturnsAllProfiles_WhenSuccessful() {
    BDDMockito.when(repository.findAll()).thenReturn(profileList);

    List<Profile> profiles = service.findAll();
    Assertions.assertThat(profiles)
        .isNotNull()
        .hasSize(profileList.size())
        .containsAll(this.profileList);
  }

  @Test
  @Order(2)
  @DisplayName("save creates a profile")
  void save_CreatesProfile_WhenSuccessful() {
    Profile profileToSave = profileUtils.newProfileToSave();
    Profile profileSaved = profileUtils.newProfileSaved();

    BDDMockito.when(repository.save(profileToSave)).thenReturn(profileSaved);

    Profile savedProfile = service.save(profileToSave);
    Assertions.assertThat(savedProfile)
        .isNotNull()
        .isEqualTo(profileSaved)
        .hasNoNullFieldsOrProperties();
  }

}