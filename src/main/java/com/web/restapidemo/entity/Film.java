package com.web.restapidemo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "film")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Thông tin về phim trong database")
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "film_id", columnDefinition = "SMALLINT UNSIGNED")
    @Schema(description = "ID duy nhất của phim", example = "1")
    private Integer filmId;

    @NotBlank(message = "Title is mandatory")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    @Column(name = "title", nullable = false)
    @Schema(description = "Tiêu đề phim", example = "Inception", required = true)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    @Schema(description = "Mô tả phim", example = "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.")
    private String description;

    @Min(value = 1901, message = "Release year must be at least 1901")
    @Max(value = 2155, message = "Release year must not exceed 2155")
    @Column(name = "release_year", columnDefinition = "YEAR")
    @Schema(description = "Năm phát hành", example = "2010")
    private Integer releaseYear;

    @NotNull(message = "Language ID is mandatory")
    @Min(value = 1, message = "Language ID must be at least 1")
    @Column(name = "language_id", nullable = false, columnDefinition = "TINYINT UNSIGNED")
    @Schema(description = "ID ngôn ngữ", example = "1", required = true)
    private Integer languageId;

    @Min(value = 1, message = "Original language ID must be at least 1")
    @Column(name = "original_language_id", columnDefinition = "TINYINT UNSIGNED")
    @Schema(description = "ID ngôn ngữ gốc", example = "2")
    private Integer originalLanguageId;

    @NotNull(message = "Rental duration is mandatory")
    @Min(value = 1, message = "Rental duration must be at least 1")
    @Max(value = 255, message = "Rental duration must not exceed 255")
    @Column(name = "rental_duration", nullable = false, columnDefinition = "TINYINT UNSIGNED")
    @Schema(description = "Thời gian cho thuê (ngày)", example = "3")
    private Integer rentalDuration = 3;

    @NotNull(message = "Rental rate is mandatory")
    @DecimalMin(value = "0.0", inclusive = false, message = "Rental rate must be greater than 0")
    @Digits(integer = 2, fraction = 2, message = "Rental rate must have format nn.nn")
    @Column(name = "rental_rate", nullable = false, precision = 4, scale = 2)
    @Schema(description = "Giá cho thuê", example = "4.99")
    private BigDecimal rentalRate = new BigDecimal("4.99");

    @Min(value = 1, message = "Length must be at least 1")
    @Column(name = "length", columnDefinition = "SMALLINT UNSIGNED")
    @Schema(description = "Độ dài phim (phút)", example = "148")
    private Integer length;

    @NotNull(message = "Replacement cost is mandatory")
    @DecimalMin(value = "0.0", inclusive = false, message = "Replacement cost must be greater than 0")
    @Digits(integer = 3, fraction = 2, message = "Replacement cost must have format nnn.nn")
    @Column(name = "replacement_cost", nullable = false, precision = 5, scale = 2)
    @Schema(description = "Chi phí thay thế", example = "19.99")
    private BigDecimal replacementCost = new BigDecimal("19.99");

    @Pattern(regexp = "^(G|PG|PG-13|R|NC-17)?$", message = "Rating must be one of: G, PG, PG-13, R, NC-17")
    @Column(name = "rating", columnDefinition = "ENUM('G','PG','PG-13','R','NC-17')")
    @Schema(description = "Đánh giá", example = "PG-13")
    private String rating = "G";

    @Column(name = "special_features", columnDefinition = "SET('Trailers','Commentaries','Deleted Scenes','Behind the Scenes')")
    @Schema(description = "Tính năng đặc biệt", example = "Trailers,Commentaries")
    private String specialFeatures;

    @UpdateTimestamp
    @Column(name = "last_update", nullable = false, updatable = false)
    @Schema(description = "Thời gian cập nhật cuối cùng", example = "2024-01-15T10:30:00")
    private LocalDateTime lastUpdate;
}