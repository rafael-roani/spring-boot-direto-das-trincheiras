package dev.rafa.userservice.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UserPutRequest {

    @Schema(description = "The user's id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @Min(value = 1, message = "The field 'id' must be greater than zero")
    @NotNull(message = "The field 'id' cannot be null")
    private Long id;

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
    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "The e-mail is not valid")
    private String email;

    private String password;

}
