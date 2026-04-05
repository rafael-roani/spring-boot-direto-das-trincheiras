package dev.rafa.animeservice.mapper;

import dev.rafa.animeservice.domain.Producer;
import dev.rafa.dto.ProducerGetResponse;
import dev.rafa.dto.ProducerPostRequest;
import dev.rafa.dto.ProducerPostResponse;
import dev.rafa.dto.ProducerPutRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProducerMapper {

    Producer toProducer(ProducerPostRequest postRequest);

    ProducerGetResponse toProducerGetResponse(Producer producer);

    List<ProducerGetResponse> toProducerGetResponseList(List<Producer> producers);

    Producer toProducer(ProducerPutRequest request);

    ProducerPostResponse toProducerPostResponse(Producer producer);

}
