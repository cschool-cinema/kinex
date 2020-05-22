package pl.termosteam.kinex.service;

import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import pl.termosteam.kinex.domain.Screening;
import pl.termosteam.kinex.exception.NotFoundException;

import static org.mockito.Mockito.mock;

public class ScreeningServiceTest {

    @Rule
    ExpectedException exception = ExpectedException.none();
    ScreeningService test = mock(ScreeningService.class);

    @Test
    public void findScreeningByIdExceptionTest(){
        exception.expect(NotFoundException.class);
        exception.expectMessage("SCREENING_NOT_FOUND");
        test.findScreeningById(-1);
    }

    @Test
    public void deleteScreeningExceptionTest1(){
        exception.expect(NotFoundException.class);
        exception.expectMessage("SCREENING_NOT_FOUND");
        test.deleteScreening(-1);
    }









}






