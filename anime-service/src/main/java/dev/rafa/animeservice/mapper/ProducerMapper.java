package dev.rafa.animeservice.mapper;

import dev.rafa.animeservice.domain.Producer;
import dev.rafa.dto.ProducerGetResponse;
import dev.rafa.dto.ProducerPostRequest;
import dev.rafa.dto.ProducerPostResponse;
import dev.rafa.dto.ProducerPutRequest;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProducerMapper {

  Producer toProducer(ProducerPostRequest postRequest);

  Producer toProducer(ProducerPutRequest request);

  ProducerGetResponse toProducerGetResponse(Producer producer);

  List<ProducerGetResponse> toProducerGetResponseList(List<Producer> producers);

  ProducerPostResponse toProducerPostResponse(Producer producer);

}
