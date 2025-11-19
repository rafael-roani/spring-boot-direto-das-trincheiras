package dev.rafa.userservice.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserProfileGetResponse {

    private Long id;

    private User user;

    private Profile profile;

    public record User(Long id, String firstName) {

    }

    public record Profile(Long id, String name) {

    }

}
