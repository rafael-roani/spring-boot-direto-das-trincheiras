package dev.rafa.userservice.commons;

import dev.rafa.userservice.domain.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserUtils {

    public List<User> newUserList() {
        User carlos = User.builder()
                .id(1L)
                .firstName("Carlos")
                .lastName("Silva")
                .email("carlos.silva@email.com")
                .build();

        User juliana = User.builder()
                .id(2L)
                .firstName("Juliana")
                .lastName("Santos")
                .email("juliana.santos@email.com")
                .build();

        User pedro = User.builder()
                .id(3L)
                .firstName("Pedro")
                .lastName("Oliveira")
                .email("pedro.oliveira@email.com")
                .build();

        return new ArrayList<>(List.of(carlos, juliana, pedro));
    }

    public User newUserToSave() {
        return User.builder()
                .firstName("Rafael")
                .lastName("Roani")
                .email("rafael.roani@email.com")
                .build();
    }

}
