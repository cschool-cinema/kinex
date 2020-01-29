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

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.termosteam.kinex.exception.StandardExceptionResponseRepository.AUDITORIUM_NOT_FOUND;

@Service
@AllArgsConstructor
public class AuditoriumService {

    private final AuditoriumRepository auditoriumRepository;

    public List<Auditorium> findAllAuditoriums() {
        return auditoriumRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    public Auditorium findAuditoriumById(int id) {
        return auditoriumRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(AUDITORIUM_NOT_FOUND));
    }

    @Transactional
    public Auditorium createAuditorium(Auditorium auditorium) {
        if (auditoriumRepository.existsByNameIgnoreCase(auditorium.getName())) {
            throw new NotAllowedException("Auditorium with this name already exists!");
        }

        List<Seat> seats = auditorium.getSeats();
        if (CollectionUtils.isEmpty(seats)) {
            throw new NotAllowedException("Please add at least one seat to auditorium!");
        }

        Set<String> seatUniqueRowAndNumber = seats.stream()
                .map(s -> s.getSeatRow().toString() + s.getSeatNumber().toString())
                .collect(Collectors.toSet());

        if (seats.size() != seatUniqueRowAndNumber.size()) {
            throw new NotAllowedException("Cannot add auditorium with duplicated seats (row, number)!");
        }

        for (Seat seat : seats) {
            seat.setAuditorium(auditorium);
        }

        return auditoriumRepository.save(auditorium);
    }
}
