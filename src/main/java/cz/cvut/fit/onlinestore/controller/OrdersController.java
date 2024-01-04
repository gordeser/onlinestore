package cz.cvut.fit.onlinestore.controller;

import cz.cvut.fit.onlinestore.dao.dto.OrderDescriptionDTO;
import cz.cvut.fit.onlinestore.dao.entity.Orders;
import cz.cvut.fit.onlinestore.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class OrdersController {
    private final OrdersService ordersService;

    @PostMapping("/api/orders")
    public ResponseEntity<Orders> createOrder(@RequestBody OrderDescriptionDTO orderDescription) {
        try {
            Orders newOrder = ordersService.createOrder(orderDescription);
            return ResponseEntity.ok(newOrder);
        } catch (Exception e) {
            System.out.println("ERROR: " + e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
