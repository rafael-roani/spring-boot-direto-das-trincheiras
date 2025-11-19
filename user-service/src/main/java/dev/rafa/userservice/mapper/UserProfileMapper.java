package dev.rafa.userservice.mapper;

import dev.rafa.userservice.domain.User;
import dev.rafa.userservice.domain.UserProfile;
import dev.rafa.userservice.response.UserProfileGetResponse;
import dev.rafa.userservice.response.UserProfileUserGetResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserProfileMapper {

    List<UserProfileGetResponse> toUserProfileGetResponse(List<UserProfile> userProfiles);

    List<UserProfileUserGetResponse> toUserProfileUserGetResponseList(List<User> users);

}
