package pl.termosteam.kinex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.termosteam.kinex.domain.Screening;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening, Integer> {

    List<Screening> findAllByScreeningStartGreaterThanEqualOrderByScreeningStart(LocalDateTime timestamp);

    @Query("SELECT COUNT(s.id) " +
            "FROM Screening s " +
            "WHERE s.auditorium.id = :auditoriumId " +
            "AND s.screeningStart < :screeningEnd " +
            "AND FUNCTION('get_screening_end', s.id) > :screeningStart")
    int screeningConflicts(LocalDateTime screeningStart, LocalDateTime screeningEnd, int auditoriumId);

    @Query("SELECT COUNT(s.id) " +
            "FROM Screening s " +
            "WHERE s.auditorium.id = :auditoriumId " +
            "AND s.id <> :excludedScreeningId " +
            "AND s.screeningStart < :screeningEnd " +
            "AND FUNCTION('get_screening_end', s.id) > :screeningStart")
    int screeningConflictsExcludingId(LocalDateTime screeningStart, LocalDateTime screeningEnd,
                                      int auditoriumId, int excludedScreeningId);

    @Query("SELECT s " +
            "FROM Screening s " +
            "WHERE LOCATE(LOWER(:title), LOWER(s.movie.title)) > 0 " +
            "AND s.screeningStart > :startingFrom " +
            "ORDER BY s.screeningStart")
    List<Screening> findByMovieTitleAndStartTime(String title, LocalDateTime startingFrom);
}
