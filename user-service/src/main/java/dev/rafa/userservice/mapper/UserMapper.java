package dev.rafa.userservice.mapper;

import dev.rafa.userservice.domain.User;
import dev.rafa.userservice.request.UserPostRequest;
import dev.rafa.userservice.request.UserPutRequest;
import dev.rafa.userservice.response.UserGetResponse;
import dev.rafa.userservice.response.UserPostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    List<UserGetResponse> toUserGetResponseList(List<User> users);

    UserGetResponse toUserGetResponse(User user);

    @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(1, 100_000))")
    User toUser(UserPostRequest userPostRequest);

    UserPostResponse toUserPostResponse(User user);

    User toUser(UserPutRequest request);

}
