package pl.termosteam.kinex.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.termosteam.kinex.repository.AuditoriumRepository;
import pl.termosteam.kinex.repository.SeatRepository;

@Service
@AllArgsConstructor
public class AuditoriumService {

    private final AuditoriumRepository auditoriumRepository;
    private final SeatRepository seatRepository;


}
