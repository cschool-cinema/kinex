package pl.termosteam.kinex.controller;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.termosteam.kinex.domain.Screening;
import pl.termosteam.kinex.dto.ScreeningDto;
import pl.termosteam.kinex.service.ScreeningService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("kinex")
@AllArgsConstructor
public class GuestController {

    private final ScreeningService screeningService;
    private final ModelMapper mm;

    @PreAuthorize("hasRole('ROLE_GUEST')")
    @GetMapping(path = "screenings")
    public List<ScreeningDto> findFutureScreenings() {
        List<Screening> screenings = screeningService.findScreeningsStartingFrom(LocalDateTime.now());

        return Arrays.asList(mm.map(screenings, ScreeningDto[].class));
    }

    @PreAuthorize("hasRole('ROLE_GUEST')")
    @GetMapping(path = "screenings/{searchPhrase}")
    public List<ScreeningDto> findFutureScreeningsByMovieTitle(@PathVariable String searchPhrase) {
        List<Screening> screenings =
                screeningService.findScreeningsByMovieTitleAndStartTime(searchPhrase, LocalDateTime.now());

        return Arrays.asList(mm.map(screenings, ScreeningDto[].class));
    }
}
