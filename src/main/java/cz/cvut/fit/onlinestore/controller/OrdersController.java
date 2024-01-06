package cz.cvut.fit.onlinestore.controller;

import cz.cvut.fit.onlinestore.dao.dto.OrdersDescriptionDTO;
import cz.cvut.fit.onlinestore.dao.dto.OrdersUpdateDTO;
import cz.cvut.fit.onlinestore.dao.dto.UsersEmailDTO;
import cz.cvut.fit.onlinestore.dao.entity.Orders;
import cz.cvut.fit.onlinestore.service.OrdersService;
import cz.cvut.fit.onlinestore.service.exceptions.OrderWithThatIdDoesNotExistException;
import cz.cvut.fit.onlinestore.service.exceptions.ProductWithThatIdDoesNotExistException;
import cz.cvut.fit.onlinestore.service.exceptions.UserWithThatEmailDoesNotExistException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Orders", description = "Access to store orders")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class OrdersController {
    private final OrdersService ordersService;

    @Operation(summary = "Get order by its id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Orders.class))}),
            @ApiResponse(responseCode = "404", description = "Order with that ID does not exist", content = { @Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema())})
    })
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

    @Operation(summary = "Create new order")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Orders.class))}),
            @ApiResponse(responseCode = "404", description = "User with that email does not exist", content = { @Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema())})
    })
    @PutMapping("/api/orders")
    public ResponseEntity<Orders> createOrder(@RequestBody OrdersDescriptionDTO orderDescription) {
        try {
            Orders newOrder = ordersService.createOrder(orderDescription);
            return ResponseEntity.ok(newOrder);
        } catch (UserWithThatEmailDoesNotExistException e) {
            System.out.println("ERROR: " + e);
            return ResponseEntity.notFound().build();
        } catch (ProductWithThatIdDoesNotExistException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            System.out.println("ERROR: " + e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<Orders>> getAllOrders() {
        try {
            return ResponseEntity.ok(ordersService.getAllOrders());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/api/orders/{id}")
    public ResponseEntity<Orders> updateOrderById(@PathVariable Long id, @RequestBody OrdersUpdateDTO orderUpdate) {
        try {
            return ResponseEntity.ok(ordersService.updateOrderById(id, orderUpdate));
        } catch (OrderWithThatIdDoesNotExistException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.out.println(e.toString());
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/api/orders/{id}")
    public ResponseEntity<Void> deleteOrderById(@PathVariable Long id) {
        try {
            ordersService.deleteOrderById(id);
            return ResponseEntity.noContent().build();
        } catch (OrderWithThatIdDoesNotExistException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Get orders by user's email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation", content = { @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Orders.class)))}),
            @ApiResponse(responseCode = "400", description = "User with that email does not exist", content = { @Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema())}),
    })
    @PostMapping("/api/orders")
    public ResponseEntity<List<Orders>> getUserOrders(@RequestBody UsersEmailDTO user) {
        try {
            List<Orders> userOrders = ordersService.getUserOrders(user.userEmail());
            return ResponseEntity.ok(userOrders);
        } catch (UserWithThatEmailDoesNotExistException e) {
            System.out.println("ERROR: " + e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            System.out.println("ERROR: " + e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
