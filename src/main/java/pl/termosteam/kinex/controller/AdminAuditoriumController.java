package pl.termosteam.kinex.controller;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.termosteam.kinex.domain.Auditorium;
import pl.termosteam.kinex.dto.AuditoriumDto;
import pl.termosteam.kinex.service.AuditoriumService;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/admin/auditorium")
public class AdminAuditoriumController {

    private final AuditoriumService auditoriumService;
    private final ModelMapper mm;

    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @GetMapping
    public AuditoriumDto[] findAllAuditoriums() {
        List<Auditorium> auditoriums = auditoriumService.findAllAuditoriums();

        return mm.map(auditoriums, AuditoriumDto[].class);
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @GetMapping(path = "{auditoriumId}")
    public AuditoriumDto findAuditoriumById(@PathVariable int auditoriumId) {
        Auditorium auditorium = auditoriumService.findAuditoriumById(auditoriumId);

        return mm.map(auditorium, AuditoriumDto.class);
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @PostMapping
    public AuditoriumDto createAuditorium(@RequestBody @Valid AuditoriumDto auditoriumDto) {
        Auditorium auditorium = mm.map(auditoriumDto, Auditorium.class);

        Auditorium savedAuditorium = auditoriumService.createAuditorium(auditorium);

        return mm.map(savedAuditorium, AuditoriumDto.class);
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @PutMapping(path = "{auditoriumId}/deactivate")
    public String deactivateAuditorium(@PathVariable int auditoriumId) {
        return auditoriumService.deactivateAuditorium(auditoriumId);
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @PutMapping(path = "{auditoriumId}/reactivate")
    public String reactivateAuditorium(@PathVariable int auditoriumId) {
        return auditoriumService.reactivateAuditorium(auditoriumId);
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @DeleteMapping(path = "{auditoriumId}")
    public String deleteAuditorium(@PathVariable int auditoriumId) {
        return auditoriumService.deleteAuditorium(auditoriumId);
    }
}