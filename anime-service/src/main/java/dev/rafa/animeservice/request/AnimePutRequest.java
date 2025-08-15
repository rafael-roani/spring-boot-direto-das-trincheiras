package dev.rafa.animeservice.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Builder
@ToString
public class AnimePutRequest {

    @Min(value = 1, message = "The field 'id' must be greater than zero")
    @NotNull(message = "The field 'id' cannot be null")
    private Long id;

    @NotBlank(message = "The field 'name' is required")
    private String name;

}
