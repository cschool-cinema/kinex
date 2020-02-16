package pl.termosteam.kinex.controller;

import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.termosteam.kinex.domain.Role;
import pl.termosteam.kinex.domain.Ticket;
import pl.termosteam.kinex.domain.User;
import pl.termosteam.kinex.dto.TicketCancelRequestDto;
import pl.termosteam.kinex.dto.TicketRequestClientDto;
import pl.termosteam.kinex.dto.TicketResponseDto;
import pl.termosteam.kinex.service.TicketService;
import pl.termosteam.kinex.service.UserService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/client/ticket")
public class ClientTicketController {

    private final TicketService ticketService;
    private final ModelMapper mm;
    private final UserService userService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(path = "make-reservation")
    public List<TicketResponseDto> makeReservation(@RequestBody @Valid TicketRequestClientDto ticketDto) {
        User user = userService.getUserNotNullIfAuthenticated();

        List<Ticket> tickets = ticketService.makeReservation(ticketDto, user, Role.USER);

        return Arrays.asList(mm.map(tickets, TicketResponseDto[].class));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(path = "cancel-reservation/{screeningId}")
    public String cancelReservationForScreening(@PathVariable int screeningId) {
        int userId = userService.getUserNotNullIfAuthenticated().getId();

        TicketCancelRequestDto ticketInfo = new TicketCancelRequestDto(userId, screeningId);

        return ticketService.cancelReservationForScreening(ticketInfo);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public List<TicketResponseDto> findUserTickets() {
        int userId = userService.getUserNotNullIfAuthenticated().getId();

        List<Ticket> tickets = ticketService.findUserTickets(userId);

        return Arrays.asList(mm.map(tickets, TicketResponseDto[].class));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(path = "screening/{screeningId}")
    public ResponseEntity<?> findUserTicketsForScreening(@PathVariable int screeningId) {
        int userId = userService.getUserNotNullIfAuthenticated().getId();

        List<Ticket> tickets = ticketService.findUserTicketsForScreening(userId, screeningId);

        if (CollectionUtils.isNotEmpty(tickets)) {
            return ResponseEntity.ok(Arrays.asList(mm.map(tickets, TicketResponseDto[].class)));
        } else {
            return ResponseEntity.ok("You have not purchased any tickets for this screening.");
        }
    }
}
