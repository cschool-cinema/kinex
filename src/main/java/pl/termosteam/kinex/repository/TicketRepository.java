package pl.termosteam.kinex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.termosteam.kinex.domain.Screening;
import pl.termosteam.kinex.domain.Ticket;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    @Query("SELECT t.seat.id FROM Ticket t WHERE t.screening.id = :screeningId AND t.active = true")
    List<Integer> findAllReservedSeatsForScreening(int screeningId);

    List<Ticket> findAllByUser_IdOrderByScreening(int userId);

    List<Ticket> findAllByUser_IdAndScreening(int userId, Screening screening);

    List<Ticket> findAllByUser_IdAndScreening_IdAndActiveTrue(int userId, int screeningId);
}