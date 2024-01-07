package cz.cvut.fit.onlinestore.controller;

import cz.cvut.fit.onlinestore.dao.dto.ProductAddDTO;
import cz.cvut.fit.onlinestore.dao.entity.Product;
import cz.cvut.fit.onlinestore.service.ProductService;
import cz.cvut.fit.onlinestore.service.exceptions.ProductWithThatIdDoesNotExistException;
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

@Tag(name = "Product", description = "Access to store products")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "Get all store products")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation", content = { @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Product.class)))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(schema = @Schema())})
    })
    @GetMapping("/api/product")
    public ResponseEntity<List<Product>> getAllProducts(@RequestParam(value = "category", required = false) String category) {
        try {
            return ResponseEntity.ok(productService.getAllProducts(category));
        } catch (Exception e) {
            System.out.println("ERROR: " + e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Get product by its id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {@Content(schema = @Schema(implementation = Product.class))}),
            @ApiResponse(responseCode = "404", description = "Product with that id does not exist", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(schema = @Schema())})
    })
    @GetMapping("/api/product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(productService.getProductById(id));
        } catch (ProductWithThatIdDoesNotExistException e) {
            System.out.println("ERROR: " + e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.out.println("ERROR: " + e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Create product")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {@Content(schema = @Schema(implementation = Product.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema())})
    })
    @PostMapping("/api/product")
    public ResponseEntity<Product> createProduct(@RequestBody ProductAddDTO product) {
        try {
            return ResponseEntity.ok(productService.createProduct(product));
        } catch (Exception e) {
            System.out.println("ERROR: " + e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Delete product by its id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successful operation", content = { @Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Product with that id does not exist", content = { @Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema())})
    })
    @DeleteMapping("/api/product/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
        try {
            productService.deleteProductById(id);
            return ResponseEntity.noContent().build();
        } catch (ProductWithThatIdDoesNotExistException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Update product by its id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {@Content(schema = @Schema(implementation = Product.class))}),
            @ApiResponse(responseCode = "404", description = "Product with that id does not exist", content = { @Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema())})
    })
    @PutMapping("/api/product/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody ProductAddDTO productUpdate) {
        try {
            return ResponseEntity.ok(productService.updateProductById(id, productUpdate));
        } catch (ProductWithThatIdDoesNotExistException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
