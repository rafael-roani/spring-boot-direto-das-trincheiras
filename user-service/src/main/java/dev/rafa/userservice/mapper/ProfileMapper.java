package dev.rafa.userservice.mapper;

import dev.rafa.userservice.domain.Profile;
import dev.rafa.userservice.request.ProfilePostRequest;
import dev.rafa.userservice.response.ProfileGetResponse;
import dev.rafa.userservice.response.ProfilePostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProfileMapper {

    Profile toProfile(ProfilePostRequest profilePostRequest);

    ProfilePostResponse toProfilePostResponse(Profile profile);

    ProfileGetResponse toProfileGetResponse(Profile profile);

    List<ProfileGetResponse> toProfileGetResponse(List<Profile> profiles);

}
