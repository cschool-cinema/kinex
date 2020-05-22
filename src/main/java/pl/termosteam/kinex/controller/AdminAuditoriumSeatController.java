package pl.termosteam.kinex.controller;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.termosteam.kinex.domain.Seat;
import pl.termosteam.kinex.dto.SeatAdminDto;
import pl.termosteam.kinex.service.SeatService;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("api/admin/auditorium")
public class AdminAuditoriumSeatController {

    private final SeatService seatService;
    private final ModelMapper mm;

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping(path = "{auditoriumId}/seat")
    public SeatAdminDto addSeat(@RequestBody @Valid SeatAdminDto seatDto, @PathVariable int auditoriumId) {
        Seat seat = mm.map(seatDto, Seat.class);

        Seat savedSeat = seatService.addSeat(seat, auditoriumId);

        return mm.map(savedSeat, SeatAdminDto.class);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PutMapping(path = "/seat/{seatId}/deactivate")
    public String deactivateSeat(@PathVariable int seatId) {
        return seatService.deactivateSeat(seatId);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PutMapping(path = "/seat/{seatId}/reactivate")
    public String reactivateSeat(@PathVariable int seatId) {
        return seatService.reactivateSeat(seatId);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @DeleteMapping(path = "/seat/{seatId}")
    public String deleteSeat(@PathVariable int seatId) {
        return seatService.deleteSeat(seatId);
    }
}