package pl.termosteam.kinex.controller;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.termosteam.kinex.domain.Seat;
import pl.termosteam.kinex.dto.SeatDto;
import pl.termosteam.kinex.service.SeatService;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("kinex")
@AllArgsConstructor
public class UserController {

    private final ModelMapper mm;
    private final SeatService seatService;

    @GetMapping(path = "screening/{screeningId}/available-seats")
    public List<SeatDto> findAvailableSeatsForScreening(@PathVariable int screeningId) {
        List<Seat> availableSeats = seatService.findAvailableSeatsForScreening(screeningId);

        return Arrays.asList(mm.map(availableSeats, SeatDto[].class));
    }
}
