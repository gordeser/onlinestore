package cz.cvut.fit.onlinestore.dao.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

// Orders because 'order' is reserved word in sql
public class Orders {
    private Long id;
    private LocalDateTime date;
    private OrderStatus status;
    private Users orderedUsers;
    private Set<Product> products = new HashSet<>();

}
