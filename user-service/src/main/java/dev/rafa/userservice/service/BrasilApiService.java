package dev.rafa.userservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.rafa.commonscore.exception.NotFoundException;
import dev.rafa.userservice.config.BrasilApiConfigurationProperties;
import dev.rafa.userservice.response.CepErrorResponse;
import dev.rafa.userservice.response.CepGetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class BrasilApiService {

    private final RestClient.Builder brasilApiClient;

    private final BrasilApiConfigurationProperties brasilApiConfigurationProperties;

    private final ObjectMapper mapper;

    public CepGetResponse findCep(String cep) {
        return brasilApiClient.build()
                .get()
                .uri(brasilApiConfigurationProperties.cepUri(), cep)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    String body = new String(response.getBody().readAllBytes());
                    CepErrorResponse cepErrorResponse = mapper.readValue(body, CepErrorResponse.class);
                    throw new NotFoundException(cepErrorResponse.toString());
                }))
                .body(CepGetResponse.class);
    }

}
