package cz.cvut.fit.onlinestore.dao.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreationTimestamp
    private LocalDateTime date;
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;
    @ManyToOne
    @JoinColumn(name = "id_users")
    private Users orderedUsers;
    @ManyToMany(mappedBy = "orders")
    private Set<Product> products;
    @Column(columnDefinition = "CLOB")
    private String productsQuantities;
}
