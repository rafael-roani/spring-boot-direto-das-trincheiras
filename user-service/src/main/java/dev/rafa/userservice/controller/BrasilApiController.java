package dev.rafa.userservice.controller;

import dev.rafa.userservice.response.CepGetResponse;
import dev.rafa.userservice.service.BrasilApiService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/brasil-api")
@SecurityRequirement(name = "basicAuth")
public class BrasilApiController {

  private final BrasilApiService service;

  @GetMapping("/cep/{cep}")
  public ResponseEntity<CepGetResponse> findCep(@PathVariable String cep) {
    return ResponseEntity.ok(service.findCep(cep));
  }

}
