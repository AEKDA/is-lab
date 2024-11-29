package islab1.models;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import islab1.exceptions.ConvertionException;
import islab1.models.auth.User;

@Entity
@Table(name = "organization")
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // Уникальный идентификатор, генерируется автоматически

    @ManyToOne(optional = false)
    @JoinColumn(name = "address_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(message = "Official address cannot be null.")
    private Address officialAddress; // Поле не может быть null

    @Column(nullable = false)
    @Positive(message = "Annual turnover must be greater than 0.")
    private int annualTurnover; // Значение поля должно быть больше 0

    @Column(nullable = false)
    @NotNull(message = "Employees count cannot be null.")
    @Positive(message = "Employees count must be greater than 0.")
    private Long employeesCount; // Поле не может быть null, значение должно быть больше 0

    @ManyToOne(optional = false)
    @JoinColumn(name = "creator_id")
    private User creator;

    @Column
    private String fullName; // Поле может быть null, строка не может быть пустой

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Type cannot be null.")
    private OrganizationType type; // Поле не может быть null

    // Getters and Setters
    public Address getOfficialAddress() {
        return officialAddress;
    }

    public void setOfficialAddress(Address officialAddress) {
        this.officialAddress = officialAddress;
    }

    public int getAnnualTurnover() {
        return annualTurnover;
    }

    public void setAnnualTurnover(int annualTurnover) {
        this.annualTurnover = annualTurnover;
    }

    public Long getEmployeesCount() {
        return employeesCount;
    }

    public void setEmployeesCount(Long employeesCount) {
        this.employeesCount = employeesCount;
    }

    public String getFullName() {
        return fullName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getCreator() {
        return creator;
    }

    public Long getId() {
        return id;
    }

    public void setCreator(User creator) throws ConvertionException {
        if (creator == null) {
            throw new ConvertionException("Creator cannot be null.");
        }
        this.creator = creator;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public OrganizationType getType() {
        return type;
    }

    public void setType(OrganizationType type) {
        this.type = type;
    }
}
