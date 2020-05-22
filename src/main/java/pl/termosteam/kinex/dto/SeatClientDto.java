package pl.termosteam.kinex.dto;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"seatRow", "seatNumber"})
public class SeatClientDto {
    private int seatId;

    @NotNull(message = "Seat row cannot be null.")
    @Min(value = 1, message = "Seat row number must be higher than 0.")
    @Max(value = 100, message = "Seat row number must must not exceed 100.")
    private Integer seatRow;

    @NotNull(message = "Seat number cannot be null.")
    @Min(value = 1, message = "Seat number must be higher than 0")
    @Max(value = 100, message = "Seat number must must not exceed 100.")
    private Integer seatNumber;
}
