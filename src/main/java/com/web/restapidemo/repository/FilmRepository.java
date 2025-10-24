package com.web.restapidemo.repository;

import com.web.restapidemo.entity.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface FilmRepository extends JpaRepository<Film, Integer> {

    List<Film> findByTitleContainingIgnoreCase(String title);
    List<Film> findByRating(String rating);
    List<Film> findByReleaseYear(Integer releaseYear);
    List<Film> findByLanguageId(Integer languageId);

    @Query("SELECT f FROM Film f WHERE f.rentalRate BETWEEN :minRate AND :maxRate")
    List<Film> findByRentalRateBetween(@Param("minRate") BigDecimal minRate, @Param("maxRate") BigDecimal maxRate);

    @Query("SELECT f FROM Film f WHERE f.length >= :minLength")
    List<Film> findByLengthGreaterThanEqual(@Param("minLength") Integer minLength);
}