package cz.cvut.fit.onlinestore.controller;

import cz.cvut.fit.onlinestore.dao.dto.ProductDescriptionDTO;
import cz.cvut.fit.onlinestore.service.ProductService;
import cz.cvut.fit.onlinestore.service.exceptions.ProductWithThatIdDoesNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductDescriptionDTO>> getAllProducts(@RequestParam(value = "category", required = false) String category) {
        return ResponseEntity.ok(productService.getAllProducts(category));
    }

    @GetMapping("/api/products/{id}")
    public ResponseEntity<ProductDescriptionDTO> getProductById(@PathVariable Long id) {
        try {
            ProductDescriptionDTO product = productService.getProductById(id);
            return ResponseEntity.ok(product);
        } catch (ProductWithThatIdDoesNotExistException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
