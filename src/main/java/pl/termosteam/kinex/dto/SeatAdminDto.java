package pl.termosteam.kinex.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeatAdminDto {
    private int seatId;

    @NotNull(message = "Seat row cannot be null.")
    @Min(value = 1, message = "Seat row number must be higher than 0.")
    @Max(value = 100, message = "Seat row number must must not exceed 100.")
    private Integer seatRow;

    @NotNull(message = "Seat number cannot be null.")
    @Min(value = 1, message = "Seat number must be higher than 0")
    @Max(value = 100, message = "Seat number must must not exceed 100.")
    private Integer seatNumber;

    @NotNull(message = "Active must be either true or false.")
    private Boolean seatActive;
}
