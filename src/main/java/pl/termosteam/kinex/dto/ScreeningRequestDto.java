package pl.termosteam.kinex.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.termosteam.kinex.validation.NowToPlusDays;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScreeningRequestDto {

    @NotNull(message = "Screening start time cannot be null!")
    @NowToPlusDays(days = 14, message = "Screening must be set between now and 14 days from now!")
    private LocalDateTime screeningStart;

    @NotNull(message = "Movie cannot be null!")
    private Integer movieId;

    @NotNull(message = "Auditorium cannot be null!")
    private Integer auditoriumId;
}
