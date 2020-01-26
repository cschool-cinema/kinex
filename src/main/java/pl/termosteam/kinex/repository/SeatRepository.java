package pl.termosteam.kinex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.termosteam.kinex.domain.Seat;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Integer> {

    List<Seat> findByActiveTrueAndAuditoriumIdOrderBySeatRowAscSeatNumberAsc(int auditoriumId);

    @Query("SELECT s FROM Seat s " +
            "WHERE s.active = true " +
            "AND s.auditorium.id = :auditoriumId " +
            "AND s.id NOT IN :excludeIds " +
            "ORDER BY s.seatRow, s.seatNumber")
    List<Seat> findByActiveAndAuditoriumIdAndExcludingSeatIds(int auditoriumId, List<Integer> excludeIds);
}