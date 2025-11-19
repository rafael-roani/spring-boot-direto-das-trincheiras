package dev.rafa.userservice.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UserProfileUserGetResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

}
