package pl.termosteam.kinex.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditoriumDto {
    private int id;

    @Size(max = 256, message = "Auditorium name character limit is 256.")
    @NotBlank(message = "Auditorium name cannot be null/blank.")
    private String name;

    private boolean active;

    private List<SeatAdminDto> seats;
}
