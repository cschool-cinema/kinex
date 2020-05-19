package pl.termosteam.kinex.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import pl.termosteam.kinex.dto.SeatAdminDto;
import pl.termosteam.kinex.service.AuditoriumService;
import pl.termosteam.kinex.domain.Auditorium;
import pl.termosteam.kinex.domain.Screening;
import pl.termosteam.kinex.domain.Seat;
import pl.termosteam.kinex.dto.AuditoriumDto;
import org.modelmapper.ModelMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@ExtendWith(MockitoExtension.class)
class AdminAuditoriumControllerTest {

    @Mock
    private AuditoriumService auditoriumService;
    @Mock
    private ModelMapper mm;
    @InjectMocks
    private AdminAuditoriumController adminAuditoriumController;
    private Auditorium auditorium1, auditorium2, auditorium3;
    private AuditoriumDto[] allAuditoriumDtoList = new AuditoriumDto[3];
    private AuditoriumDto auditoriumDto;
    private SeatAdminDto seatAdminDto1, seatAdminDto2, seatAdminDto3;
    List<Auditorium> allAuditorium = new ArrayList<Auditorium>();

    @BeforeEach
    public void init(){
        auditorium1 = new Auditorium(11, "David", true, new ArrayList<Seat>(), new ArrayList<Screening>());
        auditorium2 = new Auditorium(12, "Jack", true, new ArrayList<Seat>(), new ArrayList<Screening>());
        auditorium3 = new Auditorium(13, "Alen", true, new ArrayList<Seat>(), new ArrayList<Screening>());
        allAuditorium.add(auditorium1);
        allAuditorium.add(auditorium2);
        allAuditorium.add(auditorium3);

        seatAdminDto1 = new SeatAdminDto();
        seatAdminDto2 = new SeatAdminDto();
        seatAdminDto3 = new SeatAdminDto();
    }

    @Test
    void findAllAuditoriums() {
        when(auditoriumService.findAllAuditoriums()).thenReturn(allAuditorium);
        when(mm.map(allAuditorium, AuditoriumDto[].class)).thenReturn(allAuditoriumDtoList);
        List<AuditoriumDto> result = Arrays.asList(adminAuditoriumController.findAllAuditoriums());
        assertEquals(3, result.size());
    }

    @Test
    void findAuditoriumById() {
        Auditorium findById = allAuditorium.get(1);
        when(mm.map(findById, AuditoriumDto.class)).thenReturn(auditoriumDto);
        when(auditoriumService.findAuditoriumById(12)).thenReturn(auditorium2);
        AuditoriumDto findAuditoriumDtoById = adminAuditoriumController.findAuditoriumById(12);
        assertEquals(auditoriumDto, findAuditoriumDtoById);
    }

    @Test
    void createAuditorium() {
        Auditorium auditorium = allAuditorium.get(0);
        when(auditoriumService.createAuditorium(auditorium)).thenReturn(auditorium);
        when(mm.map(auditoriumDto, Auditorium.class)).thenReturn(auditorium);
        when(mm.map(auditorium, AuditoriumDto.class)).thenReturn(auditoriumDto);
        AuditoriumDto createAuditoriumDto = adminAuditoriumController.createAuditorium(auditoriumDto);
        assertEquals(auditoriumDto, createAuditoriumDto);
    }

    @Test
    void deactivateAuditorium() {
        Auditorium auditorium = allAuditorium.get(0);
        String result = "The auditorium and all its seats have been deactivated.";
        when(auditoriumService.deactivateAuditorium(auditorium.getId())).thenReturn(result);
        String deactiveAuditoriumResult = adminAuditoriumController.deactivateAuditorium(auditorium.getId());
        assertEquals(result, deactiveAuditoriumResult);
    }

    @Test
    void reactivateAuditorium() {
        Auditorium auditorium = allAuditorium.get(1);
        String result = "The auditorium and all its seats have been reactivated.";
        when(auditoriumService.reactivateAuditorium(auditorium.getId())).thenReturn(result);
        String reactiveAuditoriumResult = adminAuditoriumController.reactivateAuditorium(auditorium.getId());
        assertEquals(result, reactiveAuditoriumResult);
    }

    @Test
    void deleteAuditorium() {
        Auditorium auditorium = allAuditorium.get(1);
        String result = "The auditorium and all its seats were removed from database.";
        when(auditoriumService.deleteAuditorium(auditorium.getId())).thenReturn(result);
        String deleteAuditoriumResult = adminAuditoriumController.deleteAuditorium(auditorium.getId());
        assertEquals(result, deleteAuditoriumResult);
    }
}