package cz.cvut.fit.onlinestore.service;

import cz.cvut.fit.onlinestore.dao.dto.OrdersDescriptionDTO;
import cz.cvut.fit.onlinestore.dao.dto.OrdersProductsDTO;
import cz.cvut.fit.onlinestore.dao.dto.OrdersUpdateDTO;
import cz.cvut.fit.onlinestore.dao.entity.OrderStatus;
import cz.cvut.fit.onlinestore.dao.entity.Orders;
import cz.cvut.fit.onlinestore.dao.entity.Product;
import cz.cvut.fit.onlinestore.dao.entity.Users;
import cz.cvut.fit.onlinestore.dao.repository.OrdersRepository;
import cz.cvut.fit.onlinestore.dao.repository.ProductRepository;
import cz.cvut.fit.onlinestore.dao.repository.UsersRepository;
import cz.cvut.fit.onlinestore.service.exceptions.OrderWithThatIdDoesNotExistException;
import cz.cvut.fit.onlinestore.service.exceptions.ProductWithThatIdDoesNotExistException;
import cz.cvut.fit.onlinestore.service.exceptions.UserWithThatEmailDoesNotExistException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SpringBootTest
public class OrdersServiceUnitTest {
    @Autowired
    private OrdersService ordersService;
    @MockBean
    private OrdersRepository ordersRepository;
    @MockBean
    private UsersRepository usersRepository;
    @MockBean
    private ProductRepository productRepository;

    private Users user;
    private Orders order;
    private Product product;

    @BeforeEach
    void setUp() {
        user = new Users();
        order = new Orders();
        product = new Product();

        user.setId(1L);
        user.setName("name");
        user.setSurname("new surname");
        user.setEmail("email@email.com");
        user.setAddress("address");
        user.setPassword("password");
        user.setOrders(Set.of(order));

        product.setId(1L);
        product.setName("name");
        product.setDescription("desc");
        product.setPrice(1);
        product.setCategory("cat");
        product.setImage("img");
        product.setOrders(Set.of(order));

        order.setId(1L);
        order.setDate(LocalDateTime.MAX);
        order.setStatus(OrderStatus.PENDING);
        order.setOrderedUsers(user);
        order.setProduct(Set.of(product));
        order.setProductsQuantities("[{\"id\":1,\"quantity\":3}]");
    }

    @Test
    public void getOrderById() {
        Mockito.when(ordersRepository.findById(order.getId())).thenReturn(Optional.of(order));

        Orders o = ordersService.getOrderById(order.getId());
        Assertions.assertEquals(order.getDate(), o.getDate());
        Assertions.assertEquals(order.getStatus(), o.getStatus());
        Assertions.assertEquals(order.getOrderedUsers(), o.getOrderedUsers());
        Assertions.assertEquals(order.getProduct(), o.getProduct());
        Assertions.assertEquals(order.getProductsQuantities(), o.getProductsQuantities());

        Mockito.verify(ordersRepository, Mockito.times(1)).findById(order.getId());
    }

    @Test
    public void getOrderByIdWrongId() {
        Long wrongId = -1L;

        Mockito.when(ordersRepository.findById(wrongId)).thenReturn(Optional.empty());

        Assertions.assertThrows(OrderWithThatIdDoesNotExistException.class, () -> ordersService.getOrderById(wrongId));

        Mockito.verify(ordersRepository, Mockito.times(1)).findById(wrongId);
    }

    @Test
    public void getUserOrders() {
        Mockito.when(usersRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Mockito.when(ordersRepository.getOrdersByOrderedUsers(user)).thenReturn(List.of(order));

        List<Orders> oList = ordersService.getUserOrders(user.getEmail());
        Assertions.assertEquals(1, oList.size());
        Orders o = oList.get(0);

        Assertions.assertEquals(order.getDate(), o.getDate());
        Assertions.assertEquals(order.getStatus(), o.getStatus());
        Assertions.assertEquals(order.getOrderedUsers(), o.getOrderedUsers());
        Assertions.assertEquals(order.getProduct(), o.getProduct());
        Assertions.assertEquals(order.getProductsQuantities(), o.getProductsQuantities());

        Mockito.verify(usersRepository, Mockito.times(1)).findByEmail(user.getEmail());
        Mockito.verify(ordersRepository, Mockito.times(1)).getOrdersByOrderedUsers(user);
    }

    @Test
    public void getUserOrdersWrongEmail() {
        String wrongEmail = "wrong@email.com";

        Mockito.when(usersRepository.findByEmail(wrongEmail)).thenReturn(Optional.empty());
        Mockito.when(ordersRepository.getOrdersByOrderedUsers(Mockito.any(Users.class))).thenReturn(List.of(order));

        Assertions.assertThrows(UserWithThatEmailDoesNotExistException.class, () -> ordersService.getUserOrders(wrongEmail));

        Mockito.verify(usersRepository, Mockito.times(1)).findByEmail(wrongEmail);
        Mockito.verify(ordersRepository, Mockito.times(0)).getOrdersByOrderedUsers(Mockito.any(Users.class));
    }

    @Test
    public void getAllOrders() {
        Mockito.when(ordersRepository.findAll()).thenReturn(List.of(order));
        List<Orders> oList = ordersService.getAllOrders();
        Assertions.assertEquals(1, oList.size());
        Orders o = oList.get(0);

        Assertions.assertEquals(order.getDate(), o.getDate());
        Assertions.assertEquals(order.getStatus(), o.getStatus());
        Assertions.assertEquals(order.getOrderedUsers(), o.getOrderedUsers());
        Assertions.assertEquals(order.getProduct(), o.getProduct());
        Assertions.assertEquals(order.getProductsQuantities(), o.getProductsQuantities());

        Mockito.verify(ordersRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void deleteOrderById() {
        Mockito.when(ordersRepository.existsById(order.getId())).thenReturn(true);
        Mockito.doNothing().when(ordersRepository).deleteById(order.getId());

        ordersService.deleteOrderById(order.getId());

        Mockito.verify(ordersRepository, Mockito.times(1)).existsById(order.getId());
        Mockito.verify(ordersRepository, Mockito.times(1)).deleteById(order.getId());
    }

    @Test
    public void deleteOrderByIdWrongId() {
        Long wrongId = -1L;
        Mockito.when(ordersRepository.existsById(wrongId)).thenReturn(false);
        Mockito.doNothing().when(ordersRepository).deleteById(wrongId);

        Assertions.assertThrows(OrderWithThatIdDoesNotExistException.class, () -> ordersService.deleteOrderById(wrongId));

        Mockito.verify(ordersRepository, Mockito.times(1)).existsById(wrongId);
        Mockito.verify(ordersRepository, Mockito.times(0)).deleteById(wrongId);
    }

    @Test
    public void updateOrderById() {
        order.setStatus(OrderStatus.CANCELLED);

        OrdersUpdateDTO orderUpdate = new OrdersUpdateDTO(order.getDate(), OrderStatus.CANCELLED);

        Mockito.when(ordersRepository.findById(order.getId())).thenReturn(Optional.of(order));
        Mockito.when(ordersRepository.save(Mockito.any(Orders.class))).thenReturn(order);

        Orders o = ordersService.updateOrderById(order.getId(), orderUpdate);

        Assertions.assertEquals(order.getDate(), o.getDate());
        Assertions.assertEquals(OrderStatus.CANCELLED, o.getStatus());
        Assertions.assertEquals(order.getOrderedUsers(), o.getOrderedUsers());
        Assertions.assertEquals(order.getProduct(), o.getProduct());
        Assertions.assertEquals(order.getProductsQuantities(), o.getProductsQuantities());

        Mockito.verify(ordersRepository, Mockito.times(1)).findById(order.getId());
        Mockito.verify(ordersRepository, Mockito.times(1)).save(Mockito.any(Orders.class));
    }

    @Test
    public void updateOrderByIdWrongId() {
        Long wrongId = -1L;
        OrdersUpdateDTO orderUpdate = new OrdersUpdateDTO(order.getDate(), OrderStatus.CANCELLED);

        Mockito.when(ordersRepository.findById(wrongId)).thenReturn(Optional.empty());

        Assertions.assertThrows(OrderWithThatIdDoesNotExistException.class, () -> ordersService.updateOrderById(wrongId, orderUpdate));

        Mockito.verify(ordersRepository, Mockito.times(1)).findById(wrongId);
        Mockito.verify(ordersRepository, Mockito.times(0)).save(Mockito.any(Orders.class));
    }

    @Test
    public void createOrder() {
        user = new Users();
        order = new Orders();
        product = new Product();

        user.setId(1L);
        user.setName("name");
        user.setSurname("new surname");
        user.setEmail("email@email.com");
        user.setAddress("address");
        user.setPassword("password");
        user.setOrders(Set.of(order));

        product.setId(1L);
        product.setName("name");
        product.setDescription("desc");
        product.setPrice(1);
        product.setCategory("cat");
        product.setImage("img");
        product.setOrders(new HashSet<>());

        order.setId(1L);
        order.setDate(LocalDateTime.MAX);
        order.setStatus(OrderStatus.PENDING);
        order.setOrderedUsers(user);
        order.setProduct(new HashSet<>());
        order.setProductsQuantities("[{\"id\":1,\"quantity\":3}]");


        List<OrdersProductsDTO> products = List.of(new OrdersProductsDTO(product.getId(), 3));
        OrdersDescriptionDTO desc = new OrdersDescriptionDTO(user.getEmail(), products);

        Mockito.when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        Mockito.when(usersRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Mockito.when(ordersRepository.save(Mockito.any(Orders.class))).thenReturn(order);

        Orders o = ordersService.createOrder(desc);
        Assertions.assertEquals(order.getDate(), o.getDate());
        Assertions.assertEquals(order.getStatus(), o.getStatus());
        Assertions.assertEquals(order.getOrderedUsers(), o.getOrderedUsers());
        Assertions.assertEquals(order.getProduct(), o.getProduct());
        Assertions.assertEquals(order.getProductsQuantities(), o.getProductsQuantities());

        Mockito.verify(usersRepository, Mockito.times(1)).findByEmail(user.getEmail());
        Mockito.verify(productRepository, Mockito.times(1)).findById(product.getId());
        Mockito.verify(ordersRepository, Mockito.times(1)).save(Mockito.any(Orders.class));
    }

    @Test
    public void createOrderWrongEmail() {
        String wrongEmail = "wrong@email.com";

        List<OrdersProductsDTO> products = List.of(new OrdersProductsDTO(product.getId(), 3));
        OrdersDescriptionDTO desc = new OrdersDescriptionDTO(wrongEmail, products);

        Mockito.when(usersRepository.findByEmail(wrongEmail)).thenReturn(Optional.empty());


        Assertions.assertThrows(UserWithThatEmailDoesNotExistException.class, () -> ordersService.createOrder(desc));

        Mockito.verify(usersRepository, Mockito.times(1)).findByEmail(wrongEmail);
        Mockito.verify(productRepository, Mockito.times(0)).findById(product.getId());
        Mockito.verify(ordersRepository, Mockito.times(0)).save(Mockito.any(Orders.class));
    }

    @Test
    public void createOrderWrongProductId() {
        Long wrongId = -1L;

        List<OrdersProductsDTO> products = List.of(new OrdersProductsDTO(wrongId, 3));

        OrdersDescriptionDTO desc = new OrdersDescriptionDTO(user.getEmail(), products);

        Mockito.when(usersRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Mockito.when(productRepository.findById(wrongId)).thenReturn(Optional.empty());


        Assertions.assertThrows(ProductWithThatIdDoesNotExistException.class, () -> ordersService.createOrder(desc));

        Mockito.verify(usersRepository, Mockito.times(1)).findByEmail(user.getEmail());
        Mockito.verify(productRepository, Mockito.times(1)).findById(wrongId);
        Mockito.verify(ordersRepository, Mockito.times(0)).save(Mockito.any(Orders.class));
    }
}
