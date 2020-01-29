package pl.termosteam.kinex.service;

import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.termosteam.kinex.domain.Auditorium;
import pl.termosteam.kinex.domain.Seat;
import pl.termosteam.kinex.exception.NotAllowedException;
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

    public Auditorium createAuditorium(Auditorium auditorium) {
        if (auditoriumRepository.existsByNameIgnoreCase(auditorium.getName())) {
            throw new NotAllowedException("Auditorium with this name already exists!");
        }

        List<Seat> seats = auditorium.getSeats();
        if (CollectionUtils.isEmpty(seats)) {
            throw new NotAllowedException("Please add at least one seat to auditorium!");
        }

        for (Seat seat : seats) {
            seat.setAuditorium(auditorium);
        }

        return auditoriumRepository.save(auditorium);
    }
}
