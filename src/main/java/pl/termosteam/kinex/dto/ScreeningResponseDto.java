package pl.termosteam.kinex.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScreeningResponseDto {
    private int id;
    private LocalDateTime screeningStart;
    private int movieId;
    private String movieTitle;
    private short movieReleaseYear;
    private String movieCategory;
    private String movieDescription;
    private short movieDurationMin;
    private int auditoriumId;
    private String auditoriumName;
}
