package pl.termosteam.kinex.controller;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.termosteam.kinex.domain.Screening;
import pl.termosteam.kinex.domain.Seat;
import pl.termosteam.kinex.dto.ScreeningResponseDto;
import pl.termosteam.kinex.dto.SeatClientDto;
import pl.termosteam.kinex.service.ScreeningService;
import pl.termosteam.kinex.service.SeatService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/screening")
public class ScreeningController {

    private final ScreeningService screeningService;
    private final SeatService seatService;
    private final ModelMapper mm;

    @PreAuthorize("hasRole('ROLE_GUEST')")
    @GetMapping
    public List<ScreeningResponseDto> findFutureScreenings() {
        List<Screening> screenings = screeningService.findScreeningsStartingFrom(LocalDateTime.now());
        return Arrays.asList(mm.map(screenings, ScreeningResponseDto[].class));
    }

    @PreAuthorize("hasRole('ROLE_GUEST')")
    @GetMapping(path = "{title}")
    public List<ScreeningResponseDto> findFutureScreeningsByMovieTitle(@PathVariable String title) {
        List<Screening> screenings =
                screeningService.findFutureScreeningsByMovieTitle(title);

        return Arrays.asList(mm.map(screenings, ScreeningResponseDto[].class));
    }

    @PreAuthorize("hasRole('ROLE_GUEST')")
    @GetMapping(path = "{screeningId}/available-seats")
    public List<SeatClientDto> findAvailableSeatsForScreening(@PathVariable int screeningId) {
        List<Seat> availableSeats = seatService.findAvailableSeatsForScreening(screeningId);

        return Arrays.asList(mm.map(availableSeats, SeatClientDto[].class));
    }
}
