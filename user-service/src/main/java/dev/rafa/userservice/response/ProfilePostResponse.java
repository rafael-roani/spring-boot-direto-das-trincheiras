package dev.rafa.userservice.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ProfilePostResponse {

    private Long id;

    private String name;

    private String description;

}
