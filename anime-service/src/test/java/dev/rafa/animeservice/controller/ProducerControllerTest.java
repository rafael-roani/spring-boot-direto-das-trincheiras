package dev.rafa.animeservice.controller;

import dev.rafa.animeservice.domain.Producer;
import dev.rafa.animeservice.mapper.ProducerMapperImpl;
import dev.rafa.animeservice.repository.ProducerData;
import dev.rafa.animeservice.repository.ProducerHardCodedRepository;
import dev.rafa.animeservice.service.ProducerService;
import org.junit.jupiter.api.*;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebMvcTest(controllers = ProducerController.class)
@Import({ProducerMapperImpl.class, ProducerService.class, ProducerHardCodedRepository.class, ProducerData.class})
class ProducerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProducerData producerData;

    private List<Producer> producerList;

    @BeforeEach
    void init() {
        Producer ufotable = Producer.builder()
                .id(1L)
                .name("Ufotable")
                .createdAt(LocalDateTime.now())
                .build();

        Producer witStudio = Producer.builder()
                .id(2L)
                .name("Wit Studio")
                .createdAt(LocalDateTime.now())
                .build();

        Producer studioGhibli = Producer.builder()
                .id(3L)
                .name("Studio Ghibli")
                .createdAt(LocalDateTime.now())
                .build();

        producerList = new ArrayList<>(List.of(ufotable, witStudio, studioGhibli));
    }

    @Test
    @Order(1)
    @DisplayName("findAll returns a list with all producers when argument is null")
    void findAll_ReturnsAllProducers_WhenArgumentIsNull() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(1L));
    }

}
