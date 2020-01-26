package pl.termosteam.kinex.domain;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "activity_log")
@EqualsAndHashCode(of = "id")
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "activity_log_id_seq")
    @SequenceGenerator(name = "activity_log_id_seq", allocationSize = 1)
    private int id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "user_account_id_fkey"))
    private User user;

    @Column(nullable = false, updatable = false, insertable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @Size(max = 512, message = "Log message character limit is 256.")
    @NotBlank(message = "Log message cannot be null/blank.")
    private String logMessage;

    //TODO: storing request-response size set and add functionality to save for each response
    @Column(nullable = false)
    @Size(max = 512, message = "Log message character limit is 256.")
    @NotBlank(message = "Log message cannot be null/blank.")
    private String request;

    @Column(nullable = false)
    @Size(max = 512, message = "Log message character limit is 256.")
    @NotBlank(message = "Log message cannot be null/blank.")
    private String response;
}