package pl.termosteam.kinex.validation;

import pl.termosteam.kinex.dto.UserDto;

import java.time.LocalDateTime;

public interface UserDataValidation {
    /**
     * Validate if current time moment is not expired.
     *
     * @param tillDateTime day
     * @return
     */
    boolean validateDateExpired(LocalDateTime tillDateTime);

    /**
     * Validate user input data.
     *
     * @param userDTO UserDto object
     * @return
     */
    void userDataValidation(UserDto userDTO);
}
