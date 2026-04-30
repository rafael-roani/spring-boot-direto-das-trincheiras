package dev.rafa.userservice.commons;

import dev.rafa.userservice.response.CepGetResponse;
import org.springframework.stereotype.Component;

@Component
public class CepUtils {

    public CepGetResponse newCepGetResponse() {
        return CepGetResponse.builder()
                .cep("12345678")
                .city("São Paulo")
                .neighborhood("Vila Mariana")
                .street("Rua 123")
                .service("viacep")
                .build();
    }

}
