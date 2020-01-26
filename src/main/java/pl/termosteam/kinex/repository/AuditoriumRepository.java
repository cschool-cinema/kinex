package pl.termosteam.kinex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.termosteam.kinex.domain.Auditorium;

@Repository
public interface AuditoriumRepository extends JpaRepository<Auditorium, Integer> {
}
