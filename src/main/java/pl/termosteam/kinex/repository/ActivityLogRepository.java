package pl.termosteam.kinex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.termosteam.kinex.domain.ActivityLog;

import java.util.List;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Integer> {

    List<ActivityLog> findAllByUserIdOrderByCreatedAtAsc(int id);

}
