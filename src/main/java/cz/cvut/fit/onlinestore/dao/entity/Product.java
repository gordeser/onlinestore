package cz.cvut.fit.onlinestore.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    private String image;
    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private Set<Comment> comments;
    @ManyToMany
    @JoinTable(name = "product_orders",
            joinColumns = @JoinColumn(referencedColumnName = "id_product"),
            inverseJoinColumns = @JoinColumn(referencedColumnName = "id_orders"))
    @JsonIgnore
    private Set<Orders> orders;
}
