package dev.rafa.userservice.repository;

import dev.rafa.userservice.domain.User;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Component
public class UserData {

    private final List<User> users = new ArrayList<>();

    {
        users.add(
                User.builder()
                        .id(1L)
                        .firstName("Rafael")
                        .lastName("Roani Gonçalves")
                        .email("rafael@gmail.com")
                        .build()
        );
        users.add(
                User.builder()
                        .id(2L)
                        .firstName("Maria")
                        .lastName("Clara")
                        .email("maria@gmail.com")
                        .build()
        );
        users.add(
                User.builder()
                        .id(3L)
                        .firstName("Breno")
                        .lastName("Araujo")
                        .email("breno@gmail.com")
                        .build()
        );
    }

}
