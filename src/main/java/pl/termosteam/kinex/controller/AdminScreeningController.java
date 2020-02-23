package pl.termosteam.kinex.controller;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.termosteam.kinex.domain.Screening;
import pl.termosteam.kinex.dto.ScreeningRequestDto;
import pl.termosteam.kinex.dto.ScreeningResponseDto;
import pl.termosteam.kinex.service.ScreeningService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/admin/screening")
public class AdminScreeningController {

    private final ScreeningService screeningService;
    private final ModelMapper mm;

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping
    public List<ScreeningResponseDto> findAllScreenings() {
        List<Screening> screenings = screeningService.findAllScreenings();

        return Arrays.asList(mm.map(screenings, ScreeningResponseDto[].class));
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping(path = "{screeningId}")
    public ScreeningResponseDto findScreeningById(@PathVariable int screeningId) {
        Screening screening = screeningService.findScreeningById(screeningId);

        return mm.map(screening, ScreeningResponseDto.class);
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @DeleteMapping(path = "{screeningId}")
    public String deleteScreening(@PathVariable int screeningId) {
        return screeningService.deleteScreening(screeningId);
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping
    public ScreeningResponseDto createScreening(@RequestBody @Valid ScreeningRequestDto inputDto) {
        Screening screening = screeningService.createScreening(inputDto);

        return mm.map(screening, ScreeningResponseDto.class);
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PutMapping(path = "{screeningId}")
    public ScreeningResponseDto updateScreening(@PathVariable int screeningId,
                                                @RequestBody @Valid ScreeningRequestDto requestDto) {
        Screening screening = screeningService.updateScreeningDetails(requestDto, screeningId);

        return mm.map(screening, ScreeningResponseDto.class);
    }
}
