package pl.termosteam.kinex.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.termosteam.kinex.validation.MinToCurrentYearPlus;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto {

    private int id;

    @Size(max = 256, message = "Movie title character limit is 256.")
    @NotBlank(message = "Movie title cannot be null/blank.")
    private String title;

    @NotNull(message = "Release year cannot be null.")
    @MinToCurrentYearPlus(min = 1800, addToCurrentYear = 1,
            message = "The earliest release year is 1800 and the latest current year + 1.")
    private Short releaseYear;

    @Size(max = 256, message = "Category character limit is 256.")
    @NotBlank(message = "Category cannot be null/blank.")
    private String category;

    @NotNull(message = "Movie duration cannot be null.")
    @Min(value = 1, message = "Minimum movie duration is 1 minute.")
    @Max(value = 1000, message = "Maximum movie duration is 1000 minutes.")
    private Short durationMin;

    @Size(min = 1, max = 2048, message = "Description character limit is 2048.")
    private String description;
}
