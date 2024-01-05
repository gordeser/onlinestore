package cz.cvut.fit.onlinestore.controller;

import cz.cvut.fit.onlinestore.dao.dto.OrderDescriptionDTO;
import cz.cvut.fit.onlinestore.dao.entity.Orders;
import cz.cvut.fit.onlinestore.service.OrdersService;
import cz.cvut.fit.onlinestore.service.exceptions.OrderWithThatIdDoesNotExistException;
import cz.cvut.fit.onlinestore.service.exceptions.TotalAmountOutOfRangeException;
import cz.cvut.fit.onlinestore.service.exceptions.UserWithThatEmailDoesNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class OrdersController {
    private final OrdersService ordersService;

    @PostMapping("/api/orders")
    @GetMapping("/api/orders/{id}")
    public ResponseEntity<Orders> getOrderById(@PathVariable Long id) {
        try {
            Orders order = ordersService.getOrderById(id);
            return ResponseEntity.ok(order);
        } catch (OrderWithThatIdDoesNotExistException e) {
            System.out.println("ERROR: " + e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.out.println("ERROR: " + e);
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<Orders> createOrder(@RequestBody OrderDescriptionDTO orderDescription) {
        try {
            Orders newOrder = ordersService.createOrder(orderDescription);
            return ResponseEntity.ok(newOrder);
        } catch (TotalAmountOutOfRangeException e) {
            System.out.println("ERROR: " + e);
            return ResponseEntity.badRequest().build();
        } catch (UserWithThatEmailDoesNotExistException e) {
            System.out.println("ERROR: " + e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.out.println("ERROR: " + e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
