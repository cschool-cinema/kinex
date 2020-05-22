package pl.termosteam.kinex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.termosteam.kinex.domain.Auditorium;

import java.time.LocalDateTime;

@Repository
public interface AuditoriumRepository extends JpaRepository<Auditorium, Integer> {

    boolean existsByNameIgnoreCase(String name);

    @Query("SELECT (COUNT(a.id) = 1) AS exists " +
            "FROM Auditorium a " +
            "JOIN a.screenings s " +
            "WHERE a.id = :auditoriumId " +
            "AND s.screeningStart >= :dateTime")
    boolean existsScreeningsStartingFrom(int auditoriumId, LocalDateTime dateTime);
}
