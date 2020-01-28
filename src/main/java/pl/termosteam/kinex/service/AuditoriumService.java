package pl.termosteam.kinex.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.termosteam.kinex.domain.Auditorium;
import pl.termosteam.kinex.exception.NotFoundException;
import pl.termosteam.kinex.repository.AuditoriumRepository;
import pl.termosteam.kinex.repository.SeatRepository;

import java.util.List;

import static pl.termosteam.kinex.exception.StandardExceptionResponseRepository.AUDITORIUM_NOT_FOUND;

@Service
@AllArgsConstructor
public class AuditoriumService {

    private final AuditoriumRepository auditoriumRepository;
    private final SeatRepository seatRepository;

    public List<Auditorium> findAllAuditoriums() {
        return auditoriumRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    public Auditorium findAuditoriumById(int id) {
        return auditoriumRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(AUDITORIUM_NOT_FOUND));
    }
}
