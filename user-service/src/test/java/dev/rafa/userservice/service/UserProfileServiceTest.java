package dev.rafa.userservice.service;

import dev.rafa.userservice.commons.ProfileUtils;
import dev.rafa.userservice.commons.UserProfileUtils;
import dev.rafa.userservice.commons.UserUtils;
import dev.rafa.userservice.domain.User;
import dev.rafa.userservice.domain.UserProfile;
import dev.rafa.userservice.repository.UserProfileRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserProfileServiceTest {

    @InjectMocks
    private UserProfileService service;

    @Mock
    private UserProfileRepository repository;

    private List<UserProfile> userProfileList;

    @InjectMocks
    private UserProfileUtils userProfileUtils;

    @Spy
    private ProfileUtils profileUtils;

    @Spy
    private UserUtils userUtils;

    @BeforeEach
    void init() {
        userProfileList = userProfileUtils.newUserProfileList();
    }

    @Test
    @Order(1)
    @DisplayName("findAll returns a list with all user profiles")
    void findAll_ReturnsAllUserProfiles_WhenSuccessful() {
        BDDMockito.when(repository.retrieveAll()).thenReturn(userProfileList);

        List<UserProfile> userProfiles = service.findAll();
        Assertions.assertThat(userProfiles)
                .isNotNull()
                .hasSize(userProfileList.size())
                .containsAll(this.userProfileList);

        userProfiles.forEach(userProfile -> Assertions.assertThat(userProfile).hasNoNullFieldsOrProperties());
    }

    @Test
    @Order(2)
    @DisplayName("findAllUsersByProfileId returns a list of users for a given profile")
    void findAllUsersByProfileId_ReturnsAllUsersForGivenProfile_WhenSuccessful() {
        Long profileId = 99L;
        List<User> usersByProfile = this.userProfileList.stream()
                .filter(userProfile -> userProfile.getProfile().getId().equals(profileId))
                .map(UserProfile::getUser)
                .toList();

        BDDMockito.when(repository.findAllUsersByProfileId(profileId)).thenReturn(usersByProfile);

        List<User> users = service.findAllUsersByProfileId(profileId);

        Assertions.assertThat(users)
                .isNotNull()
                .hasSize(1)
                .hasSameElementsAs(usersByProfile);

        users.forEach(user -> Assertions.assertThat(user).hasNoNullFieldsOrProperties());
    }

}