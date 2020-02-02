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
public class TicketResponseDto {
    private int ticketId;
    private String movieTitle;
    private LocalDateTime screeningStartUtc;
    private String auditoriumName;
    private int seatRow;
    private int seatNumber;
    private boolean ticketActive;
}
