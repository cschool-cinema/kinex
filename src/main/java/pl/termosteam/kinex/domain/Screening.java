package pl.termosteam.kinex.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "screening", uniqueConstraints = @UniqueConstraint(columnNames = {"auditorium_id", "screening_start"},
        name = "screening_auditorium_id_screening_start_key"))
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "screening_id_seq")
    @SequenceGenerator(name = "screening_id_seq", allocationSize = 1)
    private int id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "screening_movie_id_fkey"))
    private Movie movie;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "screening_auditorium_id_fkey"))
    private Auditorium auditorium;

    @Column(name = "screening_start", columnDefinition = "timestamp (0) with time zone CHECK (screening_start>now())")
    @NotNull(message = "Screening start cannot be null.")
    private LocalDateTime screeningStartUtc;

    @OneToMany(mappedBy = "screening")
    private List<Ticket> tickets;
}
