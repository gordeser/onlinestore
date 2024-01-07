package cz.cvut.fit.onlinestore.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fit.onlinestore.dao.dto.OrdersDescriptionDTO;
import cz.cvut.fit.onlinestore.dao.dto.OrdersProductsDTO;
import cz.cvut.fit.onlinestore.dao.dto.OrdersUpdateDTO;
import cz.cvut.fit.onlinestore.dao.entity.Orders;
import cz.cvut.fit.onlinestore.dao.entity.Product;
import cz.cvut.fit.onlinestore.dao.entity.Users;
import cz.cvut.fit.onlinestore.dao.repository.OrdersRepository;
import cz.cvut.fit.onlinestore.dao.repository.ProductRepository;
import cz.cvut.fit.onlinestore.dao.repository.UsersRepository;
import cz.cvut.fit.onlinestore.service.exceptions.OrderWithThatIdDoesNotExistException;
import cz.cvut.fit.onlinestore.service.exceptions.ProductWithThatIdDoesNotExistException;
import cz.cvut.fit.onlinestore.service.exceptions.UserWithThatEmailDoesNotExistException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrdersService {
    private final OrdersRepository ordersRepository;
    private final ProductRepository productRepository;
    private final UsersRepository usersRepository;

    public void addProductToOrder(Long productId, Long orderId) {
        Orders order = ordersRepository.findById(orderId).orElseThrow(OrderWithThatIdDoesNotExistException::new);
        Product product = productRepository.findById(productId).orElseThrow(ProductWithThatIdDoesNotExistException::new);

        order.getProduct().add(product);
        ordersRepository.save(order);
    }

    public Set<Product> getProductsByOrderId(Long orderId) {
        Orders order = ordersRepository.findById(orderId).orElseThrow(OrderWithThatIdDoesNotExistException::new);
        return order.getProduct();
    }

    public void removeProductFromOrder(Long orderId, Long productId) {
        Orders order = ordersRepository.findById(orderId).orElseThrow(OrderWithThatIdDoesNotExistException::new);
        order.setProduct(
                order.getProduct().stream()
                        .filter(product -> !product.getId().equals(productId))
                        .collect(Collectors.toSet())
        );
        ordersRepository.save(order);
    }


    public Orders getOrderById(Long orderId) {
        Optional<Orders> order = ordersRepository.findById(orderId);

        if (order.isEmpty()) {
            throw new OrderWithThatIdDoesNotExistException();
        }

        return order.get();
    }

    public List<Orders> getUserOrders(String userEmail) {
        Optional<Users> user = usersRepository.findByEmail(userEmail);

        if (user.isEmpty()) {
            throw new UserWithThatEmailDoesNotExistException();
        }

        return ordersRepository.getOrdersByOrderedUsers(user.get());
    }

    public List<Orders> getAllOrders() {
        return ordersRepository.findAll();
    }

    @Transactional
    public void deleteOrderById(Long orderId) {
        if (ordersRepository.existsById(orderId)) {
            ordersRepository.deleteById(orderId);
        } else {
            throw new OrderWithThatIdDoesNotExistException();
        }
    }

    public Orders updateOrderById(Long orderId, OrdersUpdateDTO orderUpdate) {
        Optional<Orders> order = ordersRepository.findById(orderId);

        if (order.isEmpty()) {
            throw new OrderWithThatIdDoesNotExistException();
        }

        Orders updatedOrder = order.get();
        updatedOrder.setDate(updatedOrder.getDate());
        updatedOrder.setStatus(orderUpdate.status());

        return ordersRepository.save(updatedOrder);
    }

    public Orders createOrder(OrdersDescriptionDTO orderDescription) {
        Optional<Users> user = usersRepository.findByEmail(orderDescription.userEmail());
        if (user.isEmpty()) {
            throw new UserWithThatEmailDoesNotExistException();
        }

        Set<Product> productSet = orderDescription.orderProducts().stream()
                .map(productCount -> productRepository.findById(productCount.id())
                        .orElseThrow(ProductWithThatIdDoesNotExistException::new))
                .collect(Collectors.toSet());

        String quantitiesJson = convertProductCountListToJson(orderDescription.orderProducts());

        Orders newOrder = new Orders();
        newOrder.setOrderedUsers(user.get());
        newOrder.setProduct(productSet);
        newOrder.setProductsQuantities(quantitiesJson);

        for (Product p : productSet) {
            p.getOrders().add(newOrder);
        }

        return ordersRepository.save(newOrder);
    }

    private String convertProductCountListToJson(List<OrdersProductsDTO> productCounts) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(productCounts);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
