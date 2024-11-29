package islab1.models;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import islab1.exceptions.ConvertionException;
import islab1.models.auth.User;

@Entity
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // Уникальный идентификатор, генерируется автоматически

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Eye color cannot be null.")
    private Color eyeColor; // Поле не может быть null

    @ManyToOne(optional = false)
    @JoinColumn(name = "creator_id")
    private User creator;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Hair color cannot be null.")
    private Color hairColor; // Поле не может быть null

    @ManyToOne(optional = false)
    @JoinColumn(name = "location_id")
    @NotNull(message = "Location cannot be null.")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Location location; // Поле не может быть null

    @Column(nullable = false)
    @Positive(message = "Weight must be greater than 0.")
    private long weight; // Значение поля должно быть больше 0

    // Getters and Setters
    public Color getEyeColor() {
        return eyeColor;
    }

    public void setCreator(User creator) throws ConvertionException {
        if (creator == null) {
            throw new ConvertionException("Creator cannot be null.");
        }
        this.creator = creator;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getCreator() {
        return creator;
    }

    public void setEyeColor(Color eyeColor) {
        this.eyeColor = eyeColor;
    }

    public Color getHairColor() {
        return hairColor;
    }

    public void setHairColor(Color hairColor) {
        this.hairColor = hairColor;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public long getWeight() {
        return weight;
    }

    public void setWeight(long weight) {
        this.weight = weight;
    }
}
