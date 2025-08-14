package dev.rafa.userservice.domain;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

}
