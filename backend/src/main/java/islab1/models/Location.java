package islab1.models;

import javax.persistence.*;
import javax.validation.constraints.*;

import islab1.exceptions.ConvertionException;
import islab1.models.auth.User;

@Entity
@Table(name = "location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // Уникальный идентификатор, генерируется автоматически

    @Column(nullable = true)
    private double x;

    @Column(nullable = false)
    @NotNull(message = "Y cannot be null.")
    private Float y; // Поле не может быть null

    @Column(nullable = false)
    @NotNull(message = "Name cannot be null.")
    private String name; // Поле не может быть null

    @ManyToOne(optional = false)
    @JoinColumn(name = "creator_id")
    private User creator;

    public void setCreator(User creator) throws ConvertionException {
        if (creator == null) {
            throw new ConvertionException("Creator cannot be null.");
        }
        this.creator = creator;
    }

    // Getters and Setters
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public String getName() {
        return name;
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

    public void setName(String name) throws ConvertionException {
        if (name == null || name.trim().isEmpty()) {
            throw new ConvertionException("Name cannot be null or an empty string.");
        }
        this.name = name;
    }
}
