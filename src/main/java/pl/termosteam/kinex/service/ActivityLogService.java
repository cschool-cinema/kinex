package pl.termosteam.kinex.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.termosteam.kinex.domain.ActivityLog;
import pl.termosteam.kinex.domain.User;
import pl.termosteam.kinex.repository.ActivityLogRepository;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class ActivityLogService {
    private final ActivityLogRepository activityLogRepository;
    private final UserService userService;


    public List<ActivityLog> getAllActivityLogFromUser() {
        User user = userService.getUserNotNullIfAuthenticated();
        if (user != null) {
            return activityLogRepository.findAllByUserIdOrderByCreatedAtAsc(user.getId());
        }
        return Collections.emptyList();
    }
}
