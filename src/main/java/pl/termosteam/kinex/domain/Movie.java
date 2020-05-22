package pl.termosteam.kinex.domain;

import lombok.*;
import pl.termosteam.kinex.validation.MinToCurrentYearPlus;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EqualsAndHashCode(of = "id")
@Table(name = "movie", uniqueConstraints = @UniqueConstraint(
        columnNames = {"title", "release_year", "category", "duration_min"},
        name = "movie_title_release_year_category_duration_min_key"))
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movie_id_seq")
    @SequenceGenerator(name = "movie_id_seq", allocationSize = 1)
    private int id;

    @Column(nullable = false)
    @Size(max = 256, message = "Movie title character limit is 256.")
    @NotBlank(message = "Movie title cannot be null/blank.")
    private String title;

    @Column(name = "release_year", columnDefinition =
            "int2 CHECK (release_year BETWEEN 1800 AND date_part('year', " + "CURRENT_DATE)+1)")
    @NotNull(message = "Release year cannot be null.")
    @MinToCurrentYearPlus(min = 1800, addToCurrentYear = 1,
            message = "The earliest release year is 1800 and the latest current year + 1.")
    private Short releaseYear;

    @Column(nullable = false)
    @Size(max = 256, message = "Category character limit is 256.")
    @NotBlank(message = "Category cannot be null/blank.")
    private String category;

    @Column(name = "duration_min")
    @NotNull(message = "Movie duration cannot be null.")
    @Min(value = 1, message = "Minimum movie duration is 1 minute.")
    @Max(value = 1000, message = "Maximum movie duration is 1000 minutes.")
    private Short durationMin;

    @Size(min = 1, max = 2048, message = "Description character limit is 2048.")
    private String description;

    @OneToMany(mappedBy = "movie")
    private List<Screening> screenings;
}
