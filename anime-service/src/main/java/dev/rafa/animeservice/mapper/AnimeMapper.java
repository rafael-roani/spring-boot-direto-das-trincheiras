package dev.rafa.animeservice.mapper;

import dev.rafa.animeservice.domain.Anime;
import dev.rafa.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnimeMapper {

    Anime toAnime(AnimePostRequest postRequest);

    AnimePostResponse toAnimePostResponse(Anime anime);

    AnimeGetResponse toAnimeGetResponse(Anime anime);

    List<AnimeGetResponse> toAnimeGetResponseList(List<Anime> animes);

    Anime toAnime(AnimePutRequest request);

    PageAnimeGetResponse toPageAnimeGetResponse(Page<Anime> jpaPageAnime);

}
