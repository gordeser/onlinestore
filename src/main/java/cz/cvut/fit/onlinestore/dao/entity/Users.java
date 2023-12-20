package cz.cvut.fit.onlinestore.dao.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

// Users because 'user' is reserved word in sql
@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_users")
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String address;
    private String password;
    @OneToMany(mappedBy = "orderedUsers")
    private Set<Orders> orders = new HashSet<>();
}
