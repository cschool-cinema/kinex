package pl.termosteam.kinex.controller;

import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.termosteam.kinex.domain.Role;
import pl.termosteam.kinex.domain.Ticket;
import pl.termosteam.kinex.domain.User;
import pl.termosteam.kinex.dto.TicketCancelRequestDto;
import pl.termosteam.kinex.dto.TicketRequestAdminDto;
import pl.termosteam.kinex.dto.TicketRequestClientDto;
import pl.termosteam.kinex.dto.TicketResponseDto;
import pl.termosteam.kinex.service.TicketService;
import pl.termosteam.kinex.service.UserService;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/client/ticket")
public class ClientTicketController {

    private final TicketService ticketService;
    private final ModelMapper mm;
    private final UserService userService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping(path = "make-reservation")
    public TicketResponseDto[] makeReservation(@RequestBody @Valid TicketRequestClientDto ticketInfo) {
        User reservedByUser = userService.getUserNotNullIfAuthenticated();

        TicketRequestAdminDto ticketFullInfo = new TicketRequestAdminDto(
                reservedByUser.getId(),
                ticketInfo.getScreeningId(),
                ticketInfo.getSeatIds());

        List<Ticket> tickets = ticketService.makeReservation(ticketFullInfo, reservedByUser, Role.USER);

        return mm.map(tickets, TicketResponseDto[].class);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping(path = "cancel-reservation/{screeningId}")
    public String cancelReservationForScreening(@PathVariable int screeningId) {
        int userId = userService.getUserNotNullIfAuthenticated().getId();

        TicketCancelRequestDto ticketInfo = new TicketCancelRequestDto(userId, screeningId);

        return ticketService.cancelReservationForScreening(ticketInfo);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public TicketResponseDto[] findUserTickets() {
        int userId = userService.getUserNotNullIfAuthenticated().getId();

        List<Ticket> tickets = ticketService.findUserTickets(userId);

        return mm.map(tickets, TicketResponseDto[].class);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(path = "screening/{screeningId}")
    public TicketResponseDto[] findUserTicketsForScreening(@PathVariable int screeningId) {
        int userId = userService.getUserNotNullIfAuthenticated().getId();

        List<Ticket> tickets = ticketService.findUserTicketsForScreening(userId, screeningId);

        if (CollectionUtils.isNotEmpty(tickets)) {
            return mm.map(tickets, TicketResponseDto[].class);
        } else {
            return new TicketResponseDto[0];
        }
    }
}
