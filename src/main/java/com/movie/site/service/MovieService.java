package com.movie.site.service;

import com.movie.site.dto.request.*;
import com.movie.site.dto.response.GetAllDetailsMovieDtoResponse;
import com.movie.site.dto.response.GetAllMovieDtoResponse;
import com.movie.site.dto.response.GetByIdMovieDtoResponse;
import com.movie.site.dto.response.ReviewDtoResponse;
import com.movie.site.exception.ForbiddenException;
import com.movie.site.model.Movie;
import com.movie.site.model.User;
import com.movie.site.model.enums.Role;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MovieService {

    Movie create(CreateMovieDtoRequest movieDto);

    GetByIdMovieDtoResponse findById(Long id, Pageable reviewPageable);

    ReviewDtoResponse addReview(Long id, CreateReviewDtoRequest reviewDto);

    void updateReview(Long movieId, Long reviewId,
                      UpdateReviewDtoRequest reviewDto);

    void removeReview(Long movieId, Long reviewId);

    Page<ReviewDtoResponse> findAllReviews(Long id, Pageable pageable);

    Page<GetAllMovieDtoResponse> findAll(Pageable pageable, Predicate predicate);

    Movie addRating(Long id, CreateRatingDtoRequest ratingDto);

    Movie updateRating(Long id, UpdateRatingDtoRequest ratingDto);

    Movie removeRating(Long id);

    Movie findById(Long id);

    List<GetAllDetailsMovieDtoResponse> findAllByPossibleBuyer(User user,
                                                               Pageable pageable);

    static void checkPermissionToAccessMovie(Movie movie, User user) {
        if (!movie.isActive() && (user == null || !user.getAuthorities().contains(Role.ADMIN))) {
            throw new ForbiddenException();
        }
    }
}
