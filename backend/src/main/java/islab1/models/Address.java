package islab1.models;

import javax.persistence.*;
import javax.validation.constraints.*;

import islab1.exceptions.ConvertionException;
import islab1.models.auth.User;

@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // Уникальный идентификатор, генерируется автоматически

    @ManyToOne(optional = false)
    @JoinColumn(name = "creator_id")
    private User creator;

    @Column(nullable = false)
    @NotNull(message = "Zip code cannot be null.")
    private String zipCode; // Поле не может быть null

    // Getters and Setters
    public String getZipCode() {
        return zipCode;
    }

    public User getCreator() {
        return creator;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreator(User creator) throws ConvertionException {
        if (creator == null) {
            throw new ConvertionException("Creator cannot be null.");
        }
        this.creator = creator;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
