package pl.termosteam.kinex.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mm = new ModelMapper();
//        setScreeningToDto(mm);
        return mm;
    }

//    private void setScreeningToDto(ModelMapper mm) {
//        TypeMap<Screening, ScreeningResponseDto> typeMap = mm.createTypeMap(Screening.class, ScreeningResponseDto
//        .class);
//
//        typeMap.addMappings(mapper -> {
//            mapper.map(src -> src.getAuditorium().getId(), ScreeningResponseDto::setAuditoriumId);
//            mapper.map(Screening::getScreeningStart, ScreeningResponseDto::setScreeningStart);
//        });
//    }
}
