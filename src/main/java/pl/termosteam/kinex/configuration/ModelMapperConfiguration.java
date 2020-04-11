package pl.termosteam.kinex.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.termosteam.kinex.domain.Ticket;
import pl.termosteam.kinex.dto.TicketResponseDto;

@Configuration
public class ModelMapperConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mm = new ModelMapper();
        setTicketToDto(mm);
        return mm;
    }

    private void setTicketToDto(ModelMapper mm) {
        TypeMap<Ticket, TicketResponseDto> typeMap =
                mm.createTypeMap(Ticket.class, TicketResponseDto.class);
        typeMap.addMappings(mapper -> {
            mapper.map(src -> src.getScreening().getMovie().getTitle(), TicketResponseDto::setMovieTitle);
            mapper.map(src -> src.getScreening().getAuditorium().getName(), TicketResponseDto::setAuditoriumName);
        });
    }
}
