package pl.termosteam.kinex.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScreeningRequestDto {

    @NotNull(message = "Screening start time cannot be null!")
    private LocalDateTime screeningStartUtc;

    @NotNull(message = "Movie cannot be null!")
    private Integer movieId;

    @NotNull(message = "Auditorium cannot be null!")
    private Integer auditoriumId;
}
