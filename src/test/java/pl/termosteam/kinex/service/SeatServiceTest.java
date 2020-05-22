package pl.termosteam.kinex.service;

import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import pl.termosteam.kinex.exception.NotFoundException;

import static org.mockito.Mockito.mock;

public class SeatServiceTest {

    @Rule
    ExpectedException exception = ExpectedException.none();
    SeatService test = mock(SeatService.class);

    @Test
    public void findAvailableSeatsForScreeningExceptionTest() {
        exception.expect(NotFoundException.class);
        exception.expectMessage("SCREENING_NOT_FOUND");
//        test.findAvailableSeatsForScreening(-1);
    }

    }



// @Test(expected=NotFoundException.class) ?????
/*SeatService test = mock(SeatService.class);

    when(test.findAvailableSeatsForScreening(1)).thenReturn(null);

    assertNotNull(test.findAvailableSeatsForScreening(1));
*/



