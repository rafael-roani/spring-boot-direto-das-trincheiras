package dev.rafa.userservice.mapper;

import dev.rafa.userservice.annotation.EncodedMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordEncoderMapper {

    private final PasswordEncoder passwordEncoder;

    @EncodedMapping
    public String encode(String password) {
        return password == null ? null : passwordEncoder.encode(password);
    }

}
