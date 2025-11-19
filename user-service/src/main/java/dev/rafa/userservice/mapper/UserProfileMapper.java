package dev.rafa.userservice.mapper;

import dev.rafa.userservice.domain.UserProfile;
import dev.rafa.userservice.response.UserProfileGetResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserProfileMapper {

    List<UserProfileGetResponse> toUserProfileGetResponse(List<UserProfile> userProfiles);

}
