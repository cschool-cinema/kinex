package pl.termosteam.kinex.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.termosteam.kinex.domain.ActivityLog;
import pl.termosteam.kinex.dto.ActivityLogResponseDto;

@Configuration
public class ModelMapperConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mm = new ModelMapper();
        setActivityLogToDto(mm);
        return mm;
    }

    private void setActivityLogToDto(ModelMapper mm) {
        TypeMap<ActivityLog, ActivityLogResponseDto> typeMap = mm.createTypeMap(ActivityLog.class, ActivityLogResponseDto
                .class);
        typeMap.addMappings(mapper -> {
            mapper.map(src -> src.getUser().getUsername(), ActivityLogResponseDto::setUsername);
            mapper.map(src -> src.getUser().getRole(), ActivityLogResponseDto::setUserRole);
            mapper.map(ActivityLog::getCreatedAt, ActivityLogResponseDto::setCreatedAt);
            mapper.map(ActivityLog::getLogMessage, ActivityLogResponseDto::setLogMessage);
        });
    }
}
