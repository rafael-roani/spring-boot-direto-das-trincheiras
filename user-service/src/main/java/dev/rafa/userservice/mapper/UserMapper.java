package dev.rafa.userservice.mapper;

import dev.rafa.userservice.annotation.EncodedMapping;
import dev.rafa.userservice.domain.User;
import dev.rafa.userservice.request.UserPostRequest;
import dev.rafa.userservice.request.UserPutRequest;
import dev.rafa.userservice.response.UserGetResponse;
import dev.rafa.userservice.response.UserPostResponse;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = PasswordEncoderMapper.class)
public interface UserMapper {

  List<UserGetResponse> toUserGetResponseList(List<User> users);

  UserGetResponse toUserGetResponse(User user);

  @Mapping(target = "roles", constant = "USER")
  @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
  User toUser(UserPostRequest userPostRequest);

  @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
  User toUser(UserPutRequest request);

  UserPostResponse toUserPostResponse(User user);

}
