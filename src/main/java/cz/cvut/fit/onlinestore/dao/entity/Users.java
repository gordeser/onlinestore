package cz.cvut.fit.onlinestore.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

// Users because 'user' is reserved word in sql
@Entity
@Table(name = "users")
@Getter
@Setter
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_users")
    private Long id;
    private String name;
    private String surname;
    private String address;
    private String email;
    private String password;
    @OneToMany(mappedBy = "orderedUsers")
    @JsonIgnore
    private Set<Orders> orders;
}
