package pl.termosteam.kinex.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "ticket", uniqueConstraints = @UniqueConstraint(columnNames = {"screening_id", "seat_id"},
        name = "ticket_screening_id_seat_id_key"))
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticket_id_seq")
    @SequenceGenerator(name = "ticket_id_seq", allocationSize = 1)
    private int id;

    @Column(name = "user_account_id")
    @NotNull(message = "User account cannot be null.")
    private Integer userAccountId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "ticket_screening_id_fkey"))
    private Screening screening;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "ticket_seat_id_fkey"))
    private Seat seat;

    @Column(columnDefinition = "boolean DEFAULT true")
    private boolean active;

    @Column(nullable = false, updatable = false, insertable = false,
            columnDefinition = "timestamp (0) with time zone DEFAULT now()")
    private LocalDateTime timestampUtc;
}
