package cz.cvut.fit.onlinestore.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fit.onlinestore.dao.dto.OrderDescriptionDTO;
import cz.cvut.fit.onlinestore.dao.dto.ProductCountDTO;
import cz.cvut.fit.onlinestore.dao.entity.Orders;
import cz.cvut.fit.onlinestore.dao.entity.Product;
import cz.cvut.fit.onlinestore.dao.entity.Users;
import cz.cvut.fit.onlinestore.dao.repository.OrdersRepository;
import cz.cvut.fit.onlinestore.dao.repository.ProductRepository;
import cz.cvut.fit.onlinestore.dao.repository.UsersRepository;
import cz.cvut.fit.onlinestore.service.exceptions.OrderWithThatIdDoesNotExistException;
import cz.cvut.fit.onlinestore.service.exceptions.ProductWithThatIdDoesNotExistException;
import cz.cvut.fit.onlinestore.service.exceptions.TotalAmountOutOfRangeException;
import cz.cvut.fit.onlinestore.service.exceptions.UserWithThatEmailDoesNotExistException;
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

    public Orders getOrderById(Long id) {
        Optional<Orders> order = ordersRepository.getOrdersById(id);

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

    public Orders createOrder(OrderDescriptionDTO orderDescription) {
        Optional<Users> user = usersRepository.findByEmail(orderDescription.userEmail());
        if (user.isEmpty()) {
            throw new UserWithThatEmailDoesNotExistException();
        }

        Set<Product> productSet = orderDescription.orderProducts().stream()
                .map(productCount -> productRepository.findById(productCount.id())
                        .orElseThrow(ProductWithThatIdDoesNotExistException::new))
                .collect(Collectors.toSet());

        double totalAmount = 0.0;
        for (Product product : productSet) {
            totalAmount += product.getPrice();
        }

        if (totalAmount < 300 || totalAmount > 100000) {
            throw new TotalAmountOutOfRangeException();
        }

        String quantitiesJson = convertProductCountListToJson(orderDescription.orderProducts());

        Orders newOrder = new Orders();
        newOrder.setOrderedUsers(user.get());
        newOrder.setProducts(productSet);
        newOrder.setProductsQuantities(quantitiesJson);
        return ordersRepository.save(newOrder);
    }

    private String convertProductCountListToJson(List<ProductCountDTO> productCounts) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(productCounts);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
