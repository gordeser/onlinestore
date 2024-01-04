package cz.cvut.fit.onlinestore.controller;

import cz.cvut.fit.onlinestore.dao.dto.ProductDescriptionDTO;
import cz.cvut.fit.onlinestore.service.ProductService;
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
    public List<ProductDescriptionDTO> getAllProducts(
            @RequestParam(value = "category", required = false) String category
    ) {
        if (category != null && !category.isEmpty()) {
            return productService.getAllProductsWithCategory(category);
        } else {
            return productService.getAllProducts();
        }
    }

    @GetMapping("/api/products/{id}")
    public ResponseEntity<Optional<ProductDescriptionDTO>> getProductById(@PathVariable Long id) {
        Optional<ProductDescriptionDTO> product = productService.getProductById(id);
        if (product.isPresent()) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
