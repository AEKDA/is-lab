package islab1.models;

import javax.persistence.*;
import javax.validation.constraints.*;

import islab1.exceptions.ConvertionException;
import islab1.models.auth.User;

@Entity
@Table(name = "coordinates")
public class Coordinates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // Уникальный идентификатор, генерируется автоматически

    @Column(nullable = false)
    @Min(value = -426, message = "X must be greater than -427.")
    private int x; // Значение поля должно быть больше -427

    @Column(nullable = false)
    @Max(value = 653, message = "Y must be less than or equal to 653.")
    private Integer y; // Поле не может быть null

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
    public int getX() {
        return x;
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

    public void setX(int x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }
}
