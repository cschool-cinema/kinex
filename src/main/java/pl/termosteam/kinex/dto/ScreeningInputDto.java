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
public class ScreeningInputDto {

    @NotNull(message = "Screening start time cannot be null!")
    private LocalDateTime screeningStartUtc;

    @NotNull(message = "Movie id cannot be null!")
//    @Min(1)
    private int movieId;

    @NotNull(message = "Auditorium id time cannot be null!")
//    @Min(1)
    private int auditoriumId;
}
