package cz.cvut.fit.onlinestore.dao.repository;

import cz.cvut.fit.onlinestore.dao.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    Optional<Orders> getOrdersById(Long id);
}
