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

    public Orders createOrder(OrderDescriptionDTO orderDescription) {
        Optional<Users> user = usersRepository.findByEmail(orderDescription.userEmail());

        if (user.isEmpty()) {
            throw new RuntimeException("User does not exists with that email");
        } else {
            Orders newOrder = new Orders();

            newOrder.setOrderedUsers(user.get());

            Set<Product> productSet = orderDescription.orderProducts().stream()
                    .map(productCount -> productRepository.findById(productCount.id())
                            .orElseThrow(() -> new RuntimeException("Product is not found: " + productCount.id())))
                    .collect(Collectors.toSet());

            newOrder.setProducts(productSet);

            String quantitiesJson = convertProductCountListToJson(orderDescription.orderProducts());
            newOrder.setProductsQuantities(quantitiesJson);

            return ordersRepository.save(newOrder);
        }
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
