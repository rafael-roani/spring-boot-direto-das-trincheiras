package dev.rafa.userservice.commons;

import dev.rafa.userservice.response.CepErrorResponse;
import dev.rafa.userservice.response.CepGetResponse;
import dev.rafa.userservice.response.CepInnerErrorResponse;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public CepErrorResponse newCepErrorResponse() {
        CepInnerErrorResponse innerError = CepInnerErrorResponse.builder()
                .name("ServiceError")
                .message("CEP INVÁLIDO")
                .service("correios")
                .build();

        return CepErrorResponse.builder()
                .name("CepPromiseError")
                .message("Todos os serviços de CEP retornaram erro.")
                .type("service_error")
                .errors(List.of(innerError))
                .build();
    }

}
