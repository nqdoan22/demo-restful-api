package com.web.restapidemo.controller;

import com.web.restapidemo.entity.Film;
import com.web.restapidemo.service.FilmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/films")
@Tag(name = "Film Management", description = "APIs for managing films in Sakila database")
public class FilmController {

    @Autowired
    private FilmService filmService;

    // API 1: Get all films
    @Operation(summary = "Get all films", description = "Retrieve a list of all films with optional pagination")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved films")
    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    // API 2: Get film by ID
    @Operation(summary = "Get film by ID", description = "Retrieve a specific film by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Film found"),
            @ApiResponse(responseCode = "404", description = "Film not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilmById(
            @Parameter(description = "Film ID", example = "1") @PathVariable Integer id) {
        Optional<Film> film = filmService.getFilmById(id);
        return film.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // API 3: Create new film
    @Operation(summary = "Create new film", description = "Add a new film to the database")
    @ApiResponse(responseCode = "201", description = "Film created successfully")
    @PostMapping
    public ResponseEntity<Film> createFilm(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Film object to be created",
                    required = true,
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "Sample Film",
                                    value = """
                        {
                            "title": "The Matrix",
                            "description": "A computer hacker learns about the true nature of reality",
                            "releaseYear": 1999,
                            "languageId": 1,
                            "rentalDuration": 3,
                            "rentalRate": 4.99,
                            "length": 136,
                            "replacementCost": 19.99,
                            "rating": "R"
                        }
                        """
                            )
                    )
            )
            @Valid @RequestBody Film film) {
        Film createdFilm = filmService.createFilm(film);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFilm);
    }

    // API 4: Update film
    @Operation(summary = "Update film", description = "Update an existing film's information")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Film updated successfully"),
            @ApiResponse(responseCode = "404", description = "Film not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Film> updateFilm(
            @Parameter(description = "Film ID") @PathVariable Integer id,
            @Valid @RequestBody Film film) {
        Optional<Film> updatedFilm = filmService.updateFilm(id, film);
        return updatedFilm.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // API 5: Delete film
    @Operation(summary = "Delete film", description = "Remove a film from the database")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Film deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Film not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFilm(
            @Parameter(description = "Film ID") @PathVariable Integer id) {
        if (filmService.getFilmById(id).isPresent()) {
            filmService.deleteFilm(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // API 6: Search films by title
    @Operation(summary = "Search films by title", description = "Search films containing the specified title (case-insensitive)")
    @ApiResponse(responseCode = "200", description = "Search completed successfully")
    @GetMapping("/search")
    public List<Film> searchFilmsByTitle(
            @Parameter(description = "Title to search for", example = "matrix")
            @RequestParam String title) {
        return filmService.searchByTitle(title);
    }

    // API 7: Get films by rating
    @Operation(summary = "Get films by rating", description = "Retrieve all films with the specified rating")
    @ApiResponse(responseCode = "200", description = "Films retrieved successfully")
    @GetMapping("/rating/{rating}")
    public List<Film> getFilmsByRating(
            @Parameter(description = "Film rating", example = "PG-13")
            @PathVariable String rating) {
        return filmService.getFilmsByRating(rating);
    }

    // API 8: Get films by release year
    @Operation(summary = "Get films by release year", description = "Retrieve all films released in the specified year")
    @ApiResponse(responseCode = "200", description = "Films retrieved successfully")
    @GetMapping("/year/{year}")
    public List<Film> getFilmsByReleaseYear(
            @Parameter(description = "Release year", example = "2005")
            @PathVariable Integer year) {
        return filmService.getFilmsByReleaseYear(year);
    }

    // API 9: Get films by rental rate range
    @Operation(summary = "Get films by rental rate range", description = "Retrieve films within specified rental rate range")
    @ApiResponse(responseCode = "200", description = "Films retrieved successfully")
    @GetMapping("/rental-range")
    public List<Film> getFilmsByRentalRateRange(
            @Parameter(description = "Minimum rental rate", example = "2.00") @RequestParam BigDecimal minRate,
            @Parameter(description = "Maximum rental rate", example = "5.00") @RequestParam BigDecimal maxRate) {
        return filmService.getFilmsByRentalRateRange(minRate, maxRate);
    }

    // API 10: Get long films
    @Operation(summary = "Get long films", description = "Retrieve films longer than or equal to specified length")
    @ApiResponse(responseCode = "200", description = "Films retrieved successfully")
    @GetMapping("/long-films")
    public List<Film> getLongFilms(
            @Parameter(description = "Minimum length in minutes", example = "120")
            @RequestParam Integer minLength) {
        return filmService.getLongFilms(minLength);
    }
}