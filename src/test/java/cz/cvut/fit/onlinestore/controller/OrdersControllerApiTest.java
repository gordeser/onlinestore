package cz.cvut.fit.onlinestore.controller;

import cz.cvut.fit.onlinestore.dao.dto.OrdersDescriptionDTO;
import cz.cvut.fit.onlinestore.dao.dto.OrdersProductsDTO;
import cz.cvut.fit.onlinestore.dao.dto.OrdersUpdateDTO;
import cz.cvut.fit.onlinestore.dao.entity.OrderStatus;
import cz.cvut.fit.onlinestore.dao.entity.Orders;
import cz.cvut.fit.onlinestore.dao.entity.Product;
import cz.cvut.fit.onlinestore.dao.entity.Users;
import cz.cvut.fit.onlinestore.service.OrdersService;
import cz.cvut.fit.onlinestore.service.exceptions.OrderWithThatIdDoesNotExistException;
import cz.cvut.fit.onlinestore.service.exceptions.ProductWithThatIdDoesNotExistException;
import cz.cvut.fit.onlinestore.service.exceptions.UserWithThatEmailDoesNotExistException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@WebMvcTest(OrdersController.class)
public class OrdersControllerApiTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OrdersService ordersService;

    @Test
    public void getOrderById() throws Exception {
        Users user = new Users();
        Orders order = new Orders();
        Product product = new Product();

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

        Mockito.when(ordersService.getOrderById(order.getId())).thenReturn(order);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/orders/{id}", order.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        String.format("{\"id\":%d,\"date\":\"%s\",\"status\":\"%s\",\"orderedUsers\":{\"id\":%d,\"name\":\"%s\",\"surname\":\"%s\",\"address\":\"%s\",\"email\":\"%s\",\"password\":\"%s\"},\"productsQuantities\":\"[{\\\"id\\\":1,\\\"quantity\\\":3}]\"}",
                                order.getId(),
                                order.getDate(),
                                order.getStatus(),
                                order.getOrderedUsers().getId(),
                                order.getOrderedUsers().getName(),
                                order.getOrderedUsers().getSurname(),
                                order.getOrderedUsers().getAddress(),
                                order.getOrderedUsers().getEmail(),
                                order.getOrderedUsers().getPassword()
                        )));
    }

    @Test
    public void getOrderByIdWrongId() throws Exception {
        Users user = new Users();
        Orders order = new Orders();
        Product product = new Product();

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

        Mockito.when(ordersService.getOrderById(order.getId())).thenThrow(new OrderWithThatIdDoesNotExistException());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/orders/{id}", order.getId()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @Test
    public void createOrder() throws Exception {
        Users user = new Users();
        Orders order = new Orders();
        Product product = new Product();

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

        OrdersProductsDTO orderProduct = new OrdersProductsDTO(product.getId(), 3);
        OrdersDescriptionDTO desc = new OrdersDescriptionDTO(user.getEmail(), List.of(orderProduct));

        Mockito.when(ordersService.createOrder(desc)).thenReturn(order);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/api/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(String.format("{\"userEmail\":\"%s\",\"orderProducts\":[{\"id\":%d,\"quantity\":3}]}",
                                        desc.userEmail(), product.getId()
                                )))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        String.format("{\"id\":%d,\"date\":\"%s\",\"status\":\"%s\",\"orderedUsers\":{\"id\":%d,\"name\":\"%s\",\"surname\":\"%s\",\"address\":\"%s\",\"email\":\"%s\",\"password\":\"%s\"},\"productsQuantities\":\"[{\\\"id\\\":1,\\\"quantity\\\":3}]\"}",
                                order.getId(),
                                order.getDate(),
                                order.getStatus(),
                                order.getOrderedUsers().getId(),
                                order.getOrderedUsers().getName(),
                                order.getOrderedUsers().getSurname(),
                                order.getOrderedUsers().getAddress(),
                                order.getOrderedUsers().getEmail(),
                                order.getOrderedUsers().getPassword()
                        )));
    }

    @Test
    public void createOrderWrongEmail() throws Exception {
        Users user = new Users();
        Orders order = new Orders();
        Product product = new Product();

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

        OrdersProductsDTO orderProduct = new OrdersProductsDTO(product.getId(), 3);
        OrdersDescriptionDTO desc = new OrdersDescriptionDTO(user.getEmail(), List.of(orderProduct));

        Mockito.when(ordersService.createOrder(desc)).thenThrow(new UserWithThatEmailDoesNotExistException());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/api/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(String.format("{\"userEmail\":\"%s\",\"orderProducts\":[{\"id\":%d,\"quantity\":3}]}",
                                        desc.userEmail(), product.getId()
                                )))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void createOrderWrongProduct() throws Exception {
        Users user = new Users();
        Orders order = new Orders();
        Product product = new Product();

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

        OrdersProductsDTO orderProduct = new OrdersProductsDTO(product.getId(), 3);
        OrdersDescriptionDTO desc = new OrdersDescriptionDTO(user.getEmail(), List.of(orderProduct));

        Mockito.when(ordersService.createOrder(desc)).thenThrow(new ProductWithThatIdDoesNotExistException());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/api/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(String.format("{\"userEmail\":\"%s\",\"orderProducts\":[{\"id\":%d,\"quantity\":3}]}",
                                        desc.userEmail(), product.getId()
                                )))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void getAllOrders() throws Exception {
        Users user = new Users();
        Orders order = new Orders();
        Product product = new Product();

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

        Mockito.when(ordersService.getAllOrders()).thenReturn(List.of(order));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/orders"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        String.format("[{\"id\":%d,\"date\":\"%s\",\"status\":\"%s\",\"orderedUsers\":{\"id\":%d,\"name\":\"%s\",\"surname\":\"%s\",\"address\":\"%s\",\"email\":\"%s\",\"password\":\"%s\"},\"productsQuantities\":\"[{\\\"id\\\":1,\\\"quantity\\\":3}]\"}]",
                                order.getId(),
                                order.getDate(),
                                order.getStatus(),
                                order.getOrderedUsers().getId(),
                                order.getOrderedUsers().getName(),
                                order.getOrderedUsers().getSurname(),
                                order.getOrderedUsers().getAddress(),
                                order.getOrderedUsers().getEmail(),
                                order.getOrderedUsers().getPassword()
                        )));
    }

    @Test
    public void updateOrderById() throws Exception {
        Users user = new Users();
        Orders order = new Orders();
        Product product = new Product();

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
        order.setStatus(OrderStatus.DELIVERING);
        order.setOrderedUsers(user);
        order.setProduct(Set.of(product));
        order.setProductsQuantities("[{\"id\":1,\"quantity\":3}]");

        OrdersUpdateDTO orderUpdate = new OrdersUpdateDTO(LocalDateTime.MAX, OrderStatus.DELIVERING);

        Mockito.when(ordersService.updateOrderById(order.getId(), orderUpdate)).thenReturn(order);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/api/orders/{id}", order.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(String.format("{\"date\":\"%s\",\"status\":\"%s\"}",
                                        orderUpdate.date(), orderUpdate.status()
                                )))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        String.format("{\"id\":%d,\"date\":\"%s\",\"status\":\"%s\",\"orderedUsers\":{\"id\":%d,\"name\":\"%s\",\"surname\":\"%s\",\"address\":\"%s\",\"email\":\"%s\",\"password\":\"%s\"},\"productsQuantities\":\"[{\\\"id\\\":1,\\\"quantity\\\":3}]\"}",
                                order.getId(),
                                order.getDate(),
                                order.getStatus(),
                                order.getOrderedUsers().getId(),
                                order.getOrderedUsers().getName(),
                                order.getOrderedUsers().getSurname(),
                                order.getOrderedUsers().getAddress(),
                                order.getOrderedUsers().getEmail(),
                                order.getOrderedUsers().getPassword()
                        )));
    }

    @Test
    public void updateOrderByIdWrongOrder() throws Exception {
        Users user = new Users();
        Orders order = new Orders();
        Product product = new Product();

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
        order.setStatus(OrderStatus.DELIVERING);
        order.setOrderedUsers(user);
        order.setProduct(Set.of(product));
        order.setProductsQuantities("[{\"id\":1,\"quantity\":3}]");

        OrdersUpdateDTO orderUpdate = new OrdersUpdateDTO(LocalDateTime.MAX, OrderStatus.DELIVERING);

        Mockito.when(ordersService.updateOrderById(order.getId(), orderUpdate)).thenThrow(new OrderWithThatIdDoesNotExistException());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/api/orders/{id}", order.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(String.format("{\"date\":\"%s\",\"status\":\"%s\"}",
                                        orderUpdate.date(), orderUpdate.status()
                                )))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void deleteOrderById() throws Exception {
        Users user = new Users();
        Orders order = new Orders();
        Product product = new Product();

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

        Mockito.doNothing().when(ordersService).deleteOrderById(order.getId());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/api/orders/{id}", order.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void deleteOrderByIdWrongId() throws Exception {
        Users user = new Users();
        Orders order = new Orders();
        Product product = new Product();

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

        Mockito.doThrow(new OrderWithThatIdDoesNotExistException()).when(ordersService).deleteOrderById(order.getId());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/api/orders/{id}", order.getId()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getUserOrders() throws Exception {
        Users user = new Users();
        Orders order = new Orders();
        Product product = new Product();

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

        Mockito.when(ordersService.getUserOrders(user.getEmail())).thenReturn(List.of(order));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(String.format("{\"userEmail\":\"%s\"}", user.getEmail())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        String.format("[{\"id\":%d,\"date\":\"%s\",\"status\":\"%s\",\"orderedUsers\":{\"id\":%d,\"name\":\"%s\",\"surname\":\"%s\",\"address\":\"%s\",\"email\":\"%s\",\"password\":\"%s\"},\"productsQuantities\":\"[{\\\"id\\\":1,\\\"quantity\\\":3}]\"}]",
                                order.getId(),
                                order.getDate(),
                                order.getStatus(),
                                order.getOrderedUsers().getId(),
                                order.getOrderedUsers().getName(),
                                order.getOrderedUsers().getSurname(),
                                order.getOrderedUsers().getAddress(),
                                order.getOrderedUsers().getEmail(),
                                order.getOrderedUsers().getPassword()
                        )));
    }

    @Test
    public void getUserOrdersWrongUser() throws Exception {
        Users user = new Users();
        Orders order = new Orders();
        Product product = new Product();

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

        Mockito.when(ordersService.getUserOrders(user.getEmail())).thenThrow(new UserWithThatEmailDoesNotExistException());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(String.format("{\"userEmail\":\"%s\"}", user.getEmail())))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
