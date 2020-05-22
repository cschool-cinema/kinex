package pl.termosteam.kinex.validation;

import java.time.LocalDateTime;

public class DateValidation {
    public static boolean validateDateExpired(LocalDateTime tillDateTime) {
        return LocalDateTime.now().isBefore(tillDateTime);
    }

}
