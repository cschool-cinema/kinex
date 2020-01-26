package pl.termosteam.kinex.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mm = new ModelMapper();
//        setScreeningToDto(mm);
        return mm;
    }

//    private void setScreeningToDto(ModelMapper mm) {
//        TypeMap<Screening, ScreeningDto> typeMap = mm.createTypeMap(Screening.class, ScreeningDto.class);
//
//        typeMap.addMappings(mapper -> {
//            mapper.map(src -> src.getAuditorium().getId(), ScreeningDto::setAuditoriumId);
//            mapper.map(Screening::getScreeningStart, ScreeningDto::setScreeningStart);
//        });
//    }
}
