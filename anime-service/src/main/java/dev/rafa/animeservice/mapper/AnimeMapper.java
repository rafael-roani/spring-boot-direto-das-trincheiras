package dev.rafa.animeservice.mapper;

import dev.rafa.animeservice.domain.Anime;
import dev.rafa.dto.AnimeGetResponse;
import dev.rafa.dto.AnimePostRequest;
import dev.rafa.dto.AnimePostResponse;
import dev.rafa.dto.AnimePutRequest;
import dev.rafa.dto.PageAnimeGetResponse;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnimeMapper {

  Anime toAnime(AnimePostRequest postRequest);

  Anime toAnime(AnimePutRequest request);

  AnimePostResponse toAnimePostResponse(Anime anime);

  AnimeGetResponse toAnimeGetResponse(Anime anime);

  List<AnimeGetResponse> toAnimeGetResponseList(List<Anime> animes);

  PageAnimeGetResponse toPageAnimeGetResponse(Page<Anime> jpaPageAnime);

}
