package dev.rafa.animeservice.mapper;

import dev.rafa.animeservice.domain.Producer;
import dev.rafa.animeservice.request.ProducerPostRequest;
import dev.rafa.animeservice.request.ProducerPutRequest;
import dev.rafa.animeservice.response.ProducerGetResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ProducerMapper {

    ProducerMapper INSTANCE = Mappers.getMapper(ProducerMapper.class);

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(1, 100_000))")
    Producer toProducer(ProducerPostRequest postRequest);

    ProducerGetResponse toProducerGetResponse(Producer producer);

    List<ProducerGetResponse> toProducerGetResponseList(List<Producer> producers);

    Producer toProducer(ProducerPutRequest request, LocalDateTime createdAt);

}
