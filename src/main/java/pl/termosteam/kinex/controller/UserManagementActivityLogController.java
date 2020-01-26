package pl.termosteam.kinex.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.termosteam.kinex.domain.ActivityLog;
import pl.termosteam.kinex.service.ActivityLogService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/user/management/log")
public class UserManagementActivityLogController {

    private final ActivityLogService activityLogService;

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping
    public List<ActivityLog> selfActivityLog() {
        return activityLogService.getAllActivityLogFromUser();
    }
}
