package pl.termosteam.kinex.controller;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.termosteam.kinex.dto.TicketCancelRequestDto;
import pl.termosteam.kinex.service.TicketService;
import pl.termosteam.kinex.service.UserService;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("api/admin/ticket")
public class AdminTicketController {

    private final TicketService ticketService;
    private final ModelMapper mm;
    private final UserService userService;

//    @PreAuthorize("hasRole('ROLE_USER')")
//    @PostMapping(path = "make-reservation")
//    public List<TicketResponseDto> makeReservation(@RequestBody @Valid TicketRequestAdminDto ticketDto) {
//        User user = userService.getUserNotNullIfAuthenticated();
//
//        List<Ticket> tickets = ticketService.makeReservation(ticketDto, user, Role.MANAGER);
//
//        return Arrays.asList(mm.map(tickets, TicketResponseDto[].class));
//    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(path = "cancel-reservation")
    public String cancelReservationForScreening(@RequestBody @Valid TicketCancelRequestDto ticketDto) {
        return ticketService.cancelReservationForScreening(ticketDto);
    }
}
