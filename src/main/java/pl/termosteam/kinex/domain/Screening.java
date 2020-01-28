package pl.termosteam.kinex.domain;

import lombok.*;
import pl.termosteam.kinex.validation.NowToPlusDays;

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
@Table(name = "screening")
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

    @Column(name = "screening_start", columnDefinition = "timestamp (0) with time zone " +
            "CHECK (screening_start>now() AND screening_start<now() + INTERVAL '14 days')")
    @NowToPlusDays(days = 14, message = "Screening must be set between now and 14 days from now!")
    @NotNull(message = "Screening start cannot be null.")
    private LocalDateTime screeningStartUtc;

    @OneToMany(mappedBy = "screening")
    private List<Ticket> tickets;
}
