package dev.rafa.userservice.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UserPostRequest {

    @Schema(description = "The user's first name", example = "John", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "The field 'firstName' is required")
    private String firstName;

    @Schema(description = "The user's last name", example = "Doe", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "The field 'lastName' is required")
    private String lastName;

    @Schema(description = "The user's email. Must be unique",
            example = "johndoe@email.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "The field 'email' is required")
    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,10}$", message = "The e-mail is not valid")
    private String email;

    @Schema(description = "The user's password", example = "password", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "The field 'password' is required")
    private String password;

}
