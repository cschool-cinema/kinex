package pl.termosteam.kinex.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "auditorium", uniqueConstraints = @UniqueConstraint(
        name = "auditorium_name_key", columnNames = {"name"}))
@EqualsAndHashCode(of = "id")
public class Auditorium {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auditorium_id_seq")
    @SequenceGenerator(name = "auditorium_id_seq", allocationSize = 1)
    private int id;

    @Column(nullable = false)
    @Size(max = 256, message = "Auditorium name character limit is 256.")
    @NotBlank(message = "Auditorium name cannot be null/blank.")
    private String name;

    @Column(columnDefinition = "boolean DEFAULT true")
    @NotNull(message = "Active must be either true or false.")
    private Boolean active;

    @OneToMany(mappedBy = "auditorium", cascade = CascadeType.PERSIST)
    @OrderBy("seatRow, seatNumber")
    private List<Seat> seats;

    @OneToMany(mappedBy = "auditorium")
    private List<Screening> screenings;
}
