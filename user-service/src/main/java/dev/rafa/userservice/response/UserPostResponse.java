package dev.rafa.userservice.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UserPostResponse {

    @Schema(description = "The user's id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "The user's first name", example = "John", requiredMode = Schema.RequiredMode.REQUIRED)
    private String firstName;

    @Schema(description = "The user's last name", example = "Doe", requiredMode = Schema.RequiredMode.REQUIRED)
    private String lastName;

    @Schema(description = "The user's email. Must be unique",
            example = "johndoe@email.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String email;

}
