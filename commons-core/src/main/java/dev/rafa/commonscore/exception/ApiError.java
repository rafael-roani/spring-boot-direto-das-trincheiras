package dev.rafa.commonscore.exception;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {

    int status;

    String timestamp;

    String error;

    String message;

    String path;

}
