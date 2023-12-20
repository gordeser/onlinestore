package cz.cvut.fit.onlinestore.dao.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

// Orders because 'order' is reserved word in sql
@Entity
@Table(name = "orders")
@Getter
@Setter
public class Orders {
    @Id
    @Column(name = "id_orders")
    private Long id;
    private LocalDateTime date;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @ManyToOne
    @JoinColumn(name = "id_users")
    private Users orderedUsers;
    @ManyToMany
    @JoinTable(name = "product_orders",
            joinColumns = @JoinColumn(referencedColumnName = "id_orders"),
            inverseJoinColumns = @JoinColumn(referencedColumnName = "id_product"))
    private Set<Product> products = new HashSet<>();
}