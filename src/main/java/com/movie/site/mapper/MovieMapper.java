package com.movie.site.mapper;

import com.movie.site.dto.request.CreateMovieDtoRequest;
import com.movie.site.dto.response.GetAllMovieDtoResponse;
import com.movie.site.dto.response.GetByIdMovieDtoResponse;
import com.movie.site.model.Movie;
import com.movie.site.model.User;
import com.movie.site.service.AmazonS3ClientService;
import com.movie.site.service.GenreService;
import com.movie.site.service.ReviewService;
import lombok.SneakyThrows;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Mapper(uses = {GenreMapper.class, SourceDataMapper.class,
        GenreService.class, AmazonS3ClientService.class,
        ReviewService.class})
@DecoratedWith(MovieMapperDecorator.class)
public interface MovieMapper {

    @Mappings({
            @Mapping(target = "active", constant = "true"),
            @Mapping(target = "genres", qualifiedByName = "findGenresByIds")
    })
    Movie toEntity(CreateMovieDtoRequest movieDto);

    @Mappings({
            @Mapping(target = "background", source = "movie.backgroundKey",
                    qualifiedByName = "downloadFile"),
            @Mapping(target = "reviews", ignore = true),
            @Mapping(target = "totalRating", expression = "java(movie.averageRating())"),
            @Mapping(target = "numberOfRatings",
                    expression = "java(movie.numberOfRatings())"),
            @Mapping(target = "userRating", expression = "java(movie.getRatingValue(user))"),
            @Mapping(target = "userHasAlreadyWrittenReview",
                    expression = "java(movie.containsReview(user))"),
            @Mapping(target = "id", source = "movie.id")
    })
    GetByIdMovieDtoResponse toGetByIdDto(Movie movie, Pageable reviewPageable, User user);

    @Mappings({
            @Mapping(target = "poster", source = "posterKey",
                    qualifiedByName = "downloadFile"),
            @Mapping(target = "rating", expression = "java(movie.averageRating())")
    })
    GetAllMovieDtoResponse toGetAllDto(Movie movie);

    @SneakyThrows
    default Page<GetAllMovieDtoResponse> toDtoPage(Page<Movie> movies) {
        return movies.map(this::toGetAllDto);
    }
}
