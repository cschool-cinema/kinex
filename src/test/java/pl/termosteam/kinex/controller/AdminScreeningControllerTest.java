package pl.termosteam.kinex.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import pl.termosteam.kinex.domain.*;
import pl.termosteam.kinex.dto.ScreeningRequestDto;
import pl.termosteam.kinex.dto.ScreeningResponseDto;
import pl.termosteam.kinex.dto.SeatAdminDto;
import pl.termosteam.kinex.service.ScreeningService;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminScreeningControllerTest {

    @Mock
    private ScreeningService screeningService;
    @Mock
    private ModelMapper mm;
    @InjectMocks
    private AdminScreeningController adminScreeningController;
    private Screening screening1, screening2, screening3;
    private ScreeningResponseDto[] allScreenResponseDtoList = new ScreeningResponseDto[3];
    private ScreeningResponseDto screeningResponseDto;
    private ScreeningRequestDto screeningRequestDto;
    private SeatAdminDto seatAdminDto1, seatAdminDto2, seatAdminDto3;
    List<Screening> allScreening = new ArrayList<Screening>();

    @BeforeEach
    public void init(){
        screening1 = new Screening(11, new Movie(), new Auditorium(), LocalDateTime.of(2011, 4, 5, 12, 34),new ArrayList<Ticket>());
        screening2 = new Screening(12, new Movie(), new Auditorium(), LocalDateTime.of(2014, 1, 3, 17, 36),new ArrayList<Ticket>());
        screening3 = new Screening(13, new Movie(), new Auditorium(), LocalDateTime.of(2019, 7, 2, 21, 23),new ArrayList<Ticket>());
        allScreening.add(screening1);
        allScreening.add(screening2);
        allScreening.add(screening3);

        seatAdminDto1 = new SeatAdminDto();
        seatAdminDto2 = new SeatAdminDto();
        seatAdminDto3 = new SeatAdminDto();

    }

    @Test
    void findAllScreenings() {
        when(screeningService.findAllScreenings()).thenReturn(allScreening);
        when(mm.map(allScreening, ScreeningResponseDto[].class)).thenReturn(allScreenResponseDtoList);
        List<ScreeningResponseDto> result = Arrays.asList(adminScreeningController.findAllScreenings());
        assertEquals(3, result.size());
    }

    @Test
    void findScreeningById() {
        Screening findById = allScreening.get(1);
        when(mm.map(findById, ScreeningResponseDto.class)).thenReturn(screeningResponseDto);
        when(screeningService.findScreeningById(12)).thenReturn(screening2);
        ScreeningResponseDto findScreeningDtoById = adminScreeningController.findScreeningById(12);
        assertEquals(screeningResponseDto, findScreeningDtoById);
    }

    @Test
    void deleteScreening() {
        Screening screening = allScreening.get(1);
        String result = "Screening has been successfully deleted.";
        when(adminScreeningController.deleteScreening(screening.getId())).thenReturn(result);
        String deleteScreeningResult = adminScreeningController.deleteScreening(screening.getId());
        assertEquals(result, deleteScreeningResult);
    }

    @Test
    void createScreening() {
        Screening screening = allScreening.get(0);
        when(screeningService.createScreening(screeningRequestDto)).thenReturn(screening);
        when(mm.map(screening, ScreeningResponseDto.class)).thenReturn(screeningResponseDto);
        ScreeningResponseDto createScreeningDto = adminScreeningController.createScreening(screeningRequestDto);
        assertEquals(screeningRequestDto, createScreeningDto);
    }

    @Test
    void updateScreening() {
        Screening screening = allScreening.get(2);
        when(screeningService.updateScreeningDetails(screeningRequestDto, 13)).thenReturn(screening);
        when(mm.map(screening, ScreeningResponseDto.class)).thenReturn(screeningResponseDto);
        ScreeningResponseDto updateScreeningDto = adminScreeningController.updateScreening(13, screeningRequestDto);
        assertEquals(screeningRequestDto, updateScreeningDto);
    }
}