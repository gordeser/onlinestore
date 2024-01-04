package cz.cvut.fit.onlinestore.dao.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product")
@Getter
@Setter
public class Product {
    @Id
    @Column(name = "id_product")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private double price;
    private String category;
    @OneToMany(mappedBy = "product")
    private Set<Comment> comments = new HashSet<>();
    @ManyToMany
    @JoinTable(name = "product_orders",
            joinColumns = @JoinColumn(referencedColumnName = "id_product"),
            inverseJoinColumns = @JoinColumn(referencedColumnName = "id_orders"))
    private Set<Orders> orders = new HashSet<>();
}
