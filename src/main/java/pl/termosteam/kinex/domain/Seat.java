package pl.termosteam.kinex.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
@Entity
@Table(name = "seat", uniqueConstraints = @UniqueConstraint(columnNames = {"auditorium_id", "seat_row", "seat_number"},
        name = "seat_auditorium_id_seat_row_seat_number_key"))
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seat_id_seq")
    @SequenceGenerator(name = "seat_id_seq", allocationSize = 1)
    private int id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "seat_auditorium_id_fkey"))
    private Auditorium auditorium;

    @Column(name = "seat_row")
    @NotNull(message = "Seat row cannot be null.")
    @Min(value = 1, message = "Seat row number must be higher than 0.")
    @Max(value = 100, message = "Seat row number must must not exceed 100.")
    private Short seatRow;

    @Column(name = "seat_number")
    @NotNull(message = "Seat number cannot be null.")
    @Min(value = 1, message = "Seat number must be higher than 0")
    @Max(value = 100, message = "Seat number must must not exceed 100.")
    private Short seatNumber;

    @Column(columnDefinition = "boolean DEFAULT true")
    private boolean active;

    @OneToMany(mappedBy = "seat")
    private List<Ticket> tickets;
}
