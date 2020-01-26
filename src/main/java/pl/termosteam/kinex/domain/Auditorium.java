package pl.termosteam.kinex.domain;

import lombok.*;

import javax.jdo.annotations.Unique;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "auditorium")
public class Auditorium {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auditorium_id_seq")
    @SequenceGenerator(name = "auditorium_id_seq", allocationSize = 1)
    private int id;

    @Column(nullable = false)
    @Size(max = 256, message = "Auditorium name character limit is 256.")
    @NotBlank(message = "Auditorium name cannot be null/blank.")
    @Unique(name = "auditorium_name_key")
    private String name;

    @OneToMany(mappedBy = "auditorium")
    private List<Seat> seats;

    @OneToMany(mappedBy = "auditorium")
    private List<Screening> screenings;
}
