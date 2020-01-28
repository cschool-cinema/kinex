package pl.termosteam.kinex.controller;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.termosteam.kinex.domain.Auditorium;
import pl.termosteam.kinex.dto.AuditoriumDto;
import pl.termosteam.kinex.service.AuditoriumService;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/administration/auditorium")
public class AdministrationAuditoriumController {

    private final AuditoriumService auditoriumService;
    private final ModelMapper mm;

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping
    public List<AuditoriumDto> findAllAuditoriums() {
        List<Auditorium> auditoriums = auditoriumService.findAllAuditoriums();

        return Arrays.asList(mm.map(auditoriums, AuditoriumDto[].class));
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping(path = "{auditoriumId}")
    public AuditoriumDto findAuditoriumById(@PathVariable int auditoriumId) {
        Auditorium auditorium = auditoriumService.findAuditoriumById(auditoriumId);

        return mm.map(auditorium, AuditoriumDto.class);
    }
}