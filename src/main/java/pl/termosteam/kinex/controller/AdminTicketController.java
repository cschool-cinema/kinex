package pl.termosteam.kinex.controller;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.termosteam.kinex.domain.Role;
import pl.termosteam.kinex.domain.Ticket;
import pl.termosteam.kinex.domain.User;
import pl.termosteam.kinex.dto.TicketCancelRequestDto;
import pl.termosteam.kinex.dto.TicketRequestAdminDto;
import pl.termosteam.kinex.dto.TicketResponseDto;
import pl.termosteam.kinex.service.TicketService;
import pl.termosteam.kinex.service.UserService;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/admin/ticket")
public class AdminTicketController {

    private final TicketService ticketService;
    private final ModelMapper mm;
    private final UserService userService;

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping(path = "make-reservation")
    public TicketResponseDto[] makeReservation(@RequestBody @Valid TicketRequestAdminDto ticketInfo) {
        User reservedByUser = userService.getUserNotNullIfAuthenticated();

        List<Ticket> tickets = ticketService.makeReservation(ticketInfo, reservedByUser, Role.MANAGER);

        return mm.map(tickets, TicketResponseDto[].class);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping(path = "cancel-reservation")
    public String cancelReservationForScreening(@RequestBody @Valid TicketCancelRequestDto ticketDto) {
        return ticketService.cancelReservationForScreening(ticketDto);
    }
}
