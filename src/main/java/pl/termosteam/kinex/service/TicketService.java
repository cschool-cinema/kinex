package pl.termosteam.kinex.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.termosteam.kinex.domain.Screening;
import pl.termosteam.kinex.domain.Ticket;
import pl.termosteam.kinex.exception.NotFoundException;
import pl.termosteam.kinex.repository.ScreeningRepository;
import pl.termosteam.kinex.repository.TicketRepository;

import java.util.List;

import static pl.termosteam.kinex.exception.StandardExceptionResponseRepository.SCREENING_NOT_FOUND;

@Service
@AllArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ScreeningRepository screeningRepository;

    public List<Ticket> findUserTickets(int userId) {
        return ticketRepository.findAllByUser_IdOrderByScreening(userId);
    }

    public List<Ticket> findUserTicketsForScreening(int userId, int screeningId) {
        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new NotFoundException(SCREENING_NOT_FOUND));

        return ticketRepository.findAllByUser_IdAndScreening(userId, screening);
    }
}
