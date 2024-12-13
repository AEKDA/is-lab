package islab1.models;

import java.time.LocalDateTime;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.annotations.CreationTimestamp;

import islab1.exceptions.ConvertionException;
import islab1.models.auth.User;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "history_imports")
@Getter
@Setter
public class HistoryImport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // Уникальный идентификатор, генерируется автоматически

    @Column(nullable = false)
    @NotNull(message = "createdAt cannot be null.")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Lob
    private String fileLink;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "status cannot be null.")
    private StatusImport status;

    @Column(nullable = false)
    @NotNull(message = "count cannot be null.")
    private int count;

    @ManyToOne(optional = false)
    @JoinColumn(name = "creator_id")
    private User creator;

    public void setCreator(User creator) throws ConvertionException {
        if (creator == null) {
            throw new ConvertionException("Creator cannot be null.");
        }
        this.creator = creator;
    }

}
