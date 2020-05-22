package pl.termosteam.kinex.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketRequestClientDto {

    @NotNull(message = "Please choose screening.")
    private Integer screeningId;

    @NotNull(message = "Please choose at least one seat.")
    private int[] seatIds;
}
