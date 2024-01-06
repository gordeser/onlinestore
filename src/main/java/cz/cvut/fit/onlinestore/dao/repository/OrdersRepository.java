package cz.cvut.fit.onlinestore.dao.repository;

import cz.cvut.fit.onlinestore.dao.entity.OrderStatus;
import cz.cvut.fit.onlinestore.dao.entity.Orders;
import cz.cvut.fit.onlinestore.dao.entity.Users;
import jakarta.transaction.Transactional;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    List<Orders> getOrdersByOrderedUsers(Users user);

    @Transactional
    @Modifying
    @Query("UPDATE Orders o SET o.date = :date, o.status = :status WHERE o.id = :id")
    int updateOrder(Long id, LocalDateTime date, OrderStatus status);
}
