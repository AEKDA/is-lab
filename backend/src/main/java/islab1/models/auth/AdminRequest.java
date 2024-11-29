package islab1.models.auth;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "admin_request")
public class AdminRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)  // Поле не может быть null, уникальное, генерируется автоматически
    private long id;

    @Column(unique = true)
    private String username;
    private String password;

    @Column(nullable = true)  // Поле может быть null
    private String comment;

    @ManyToOne
    @JoinColumn(name = "reviewer_id")  // Поле может быть null
    private User reviewer;
}
