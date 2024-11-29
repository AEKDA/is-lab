package islab1.models;

import java.time.LocalDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import islab1.exceptions.ConvertionException;
import islab1.models.auth.User;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "worker")
@Getter
@Setter
public class Worker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private long id; // Значение генерируется автоматически, уникальное

    @ManyToOne(optional = false)
    @JoinColumn(name = "creator_id")
    @NotNull(message = "Creator cannot be null.")
    private User creator;

    @Column(nullable = false)
    @NotBlank(message = "Name cannot be null or an empty string.")
    private String name; // Поле не может быть null, строка не может быть пустой

    @ManyToOne(optional = false)
    @JoinColumn(name = "coordinates_id")
    @NotNull(message = "Coordinates cannot be null.")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Coordinates coordinates; // Поле не может быть null

    @Column(nullable = false, updatable = false)
    @NotNull(message = "Creation date cannot be null.")
    private LocalDateTime creationDate; // Поле не может быть null, генерируется автоматически

    @ManyToOne(optional = false)
    @JoinColumn(name = "organization_id")
    @NotNull(message = "Organization cannot be null.")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Organization organization; // Поле не может быть null

    @Column(nullable = false)
    @NotNull(message = "Salary cannot be null.")
    @Positive(message = "Salary must be greater than 0.")
    private Long salary; // Поле не может быть null, значение должно быть больше 0

    @Column
    @Positive(message = "Rating must be greater than 0.")
    private Double rating; // Поле может быть null, значение должно быть больше 0

    @Column(nullable = false)
    @NotNull(message = "Start date cannot be null.")
    private java.util.Date startDate; // Поле не может быть null

    @Column(nullable = false)
    @NotNull(message = "End date cannot be null.")
    private LocalDateTime endDate; // Поле может быть null

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Position cannot be null.")
    private Position position; // Поле не может быть null

    @ManyToOne(optional = true)
    @JoinColumn(name = "person_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Person person; // Поле может быть null

    // Метод для автоматической установки creationDate при создании записи
    @PrePersist
    protected void onCreate() {
        this.creationDate = LocalDateTime.now();
    }

    // Сеттеры с валидацией
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or an empty string.");
        }
        this.name = name;
    }

    public void setCoordinates(Coordinates coordinates) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Coordinates cannot be null.");
        }
        this.coordinates = coordinates;
    }

    public void setSalary(Long salary) {
        if (salary == null || salary <= 0) {
            throw new IllegalArgumentException("Salary must be greater than 0.");
        }
        this.salary = salary;
    }

    public void setCreator(User creator) throws ConvertionException {
        if (creator == null) {
            throw new ConvertionException("Creator cannot be null.");
        }
        this.creator = creator;
    }

    public void setRating(Double rating) {
        if (rating != null && rating <= 0) {
            throw new IllegalArgumentException("Rating must be greater than 0.");
        }
        this.rating = rating;
    }

    public void setStartDate(java.util.Date startDate) {
        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null.");
        }
        this.startDate = startDate;
    }

    public void setPosition(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null.");
        }
        this.position = position;
    }

    public void setOrganization(Organization organization) {
        if (organization == null) {
            throw new IllegalArgumentException("Organization cannot be null.");
        }
        this.organization = organization;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate; // Можно устанавливать null
    }

    public void setPerson(Person person) {
        this.person = person; // Можно устанавливать null
    }
}
