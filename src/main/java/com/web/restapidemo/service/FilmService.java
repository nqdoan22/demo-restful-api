package com.web.restapidemo.service;

import com.web.restapidemo.entity.Film;
import com.web.restapidemo.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class FilmService {

    @Autowired
    private FilmRepository filmRepository;

    public List<Film> getAllFilms() {
        return filmRepository.findAll();
    }

    public Optional<Film> getFilmById(Integer id) {
        return filmRepository.findById(id);
    }

    public Film createFilm(@Valid Film film) {
        return filmRepository.save(film);
    }

    public Optional<Film> updateFilm(Integer id, @Valid Film filmDetails) {
        return filmRepository.findById(id).map(existingFilm -> {
            existingFilm.setTitle(filmDetails.getTitle());
            existingFilm.setDescription(filmDetails.getDescription());
            existingFilm.setReleaseYear(filmDetails.getReleaseYear());
            existingFilm.setLanguageId(filmDetails.getLanguageId());
            existingFilm.setOriginalLanguageId(filmDetails.getOriginalLanguageId());
            existingFilm.setRentalDuration(filmDetails.getRentalDuration());
            existingFilm.setRentalRate(filmDetails.getRentalRate());
            existingFilm.setLength(filmDetails.getLength());
            existingFilm.setReplacementCost(filmDetails.getReplacementCost());
            existingFilm.setRating(filmDetails.getRating());
            existingFilm.setSpecialFeatures(filmDetails.getSpecialFeatures());
            return filmRepository.save(existingFilm);
        });
    }

    public void deleteFilm(Integer id) {
        filmRepository.deleteById(id);
    }

    // Search methods
    public List<Film> searchByTitle(String title) {
        return filmRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Film> getFilmsByRating(String rating) {
        return filmRepository.findByRating(rating);
    }

    public List<Film> getFilmsByReleaseYear(Integer year) {
        return filmRepository.findByReleaseYear(year);
    }

    public List<Film> getFilmsByRentalRateRange(BigDecimal minRate, BigDecimal maxRate) {
        return filmRepository.findByRentalRateBetween(minRate, maxRate);
    }

    public List<Film> getLongFilms(Integer minLength) {
        return filmRepository.findByLengthGreaterThanEqual(minLength);
    }
}