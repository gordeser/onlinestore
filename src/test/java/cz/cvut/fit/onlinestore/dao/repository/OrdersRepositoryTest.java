package cz.cvut.fit.onlinestore.dao.repository;

import cz.cvut.fit.onlinestore.dao.entity.Orders;
import cz.cvut.fit.onlinestore.dao.entity.Product;
import cz.cvut.fit.onlinestore.dao.entity.Users;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Set;

@DataJpaTest
public class OrdersRepositoryTest {
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private ProductRepository productRepository;

    @Test
    public void getOrdersByOrderedUsersPositive() {
        var u1 = new Users();
        var o1 = new Orders();
        var p1 = new Product();
        var p2 = new Product();

        p1.setId(1L);
        p1.setName("first");
        p2.setId(2L);
        p2.setName("second");

        u1.setId(1L);

        o1.setOrderedUsers(u1);
        o1.setProduct(Set.of(p1, p2));

        productRepository.save(p1);
        productRepository.save(p2);
        usersRepository.save(u1);
        ordersRepository.save(o1);


        List<Orders> orders = ordersRepository.getOrdersByOrderedUsers(u1);
        Assertions.assertEquals(1, orders.size());

        Orders getOrder = orders.get(0);

        Assertions.assertEquals(o1.getId(), getOrder.getId());
        Assertions.assertEquals(u1.getId(), getOrder.getOrderedUsers().getId());
        Assertions.assertEquals(o1.getProduct(), getOrder.getProduct());
    }

    @Test
    public void getOrdersByOrderedUsersFalse() {
        var u1 = new Users();
        var u2 = new Users();
        var o1 = new Orders();
        var p1 = new Product();
        var p2 = new Product();

        p1.setId(1L);
        p1.setName("first");
        p2.setId(2L);
        p2.setName("second");

        u1.setId(1L);
        u2.setId(2L);

        o1.setOrderedUsers(u1);
        o1.setProduct(Set.of(p1, p2));

        productRepository.save(p1);
        productRepository.save(p2);
        usersRepository.save(u1);
        usersRepository.save(u2);
        ordersRepository.save(o1);


        List<Orders> orders = ordersRepository.getOrdersByOrderedUsers(u2);
        Assertions.assertTrue(orders.isEmpty());
    }
}
