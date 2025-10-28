package dev.rafa.animeservice.mapper;

import dev.rafa.animeservice.domain.Anime;
import dev.rafa.animeservice.request.AnimePostRequest;
import dev.rafa.animeservice.request.AnimePutRequest;
import dev.rafa.animeservice.response.AnimeGetResponse;
import dev.rafa.animeservice.response.AnimePostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnimeMapper {

    Anime toAnime(AnimePostRequest postRequest);

    AnimePostResponse toAnimePostResponse(Anime anime);

    AnimeGetResponse toAnimeGetResponse(Anime anime);

    List<AnimeGetResponse> toAnimeGetResponseList(List<Anime> animes);

    Anime toAnime(AnimePutRequest request);

}
