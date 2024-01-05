package cz.cvut.fit.onlinestore.controller;

import cz.cvut.fit.onlinestore.dao.dto.ProductDescriptionDTO;
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

    @Operation(summary = "Get store products")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation", content = { @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProductDescriptionDTO.class)))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(schema = @Schema())})
    })
    @GetMapping("/api/product")
    public ResponseEntity<List<ProductDescriptionDTO>> getAllProducts(@RequestParam(value = "category", required = false) String category) {
        try {
            return ResponseEntity.ok(productService.getAllProducts(category));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Get product by its id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {@Content(schema = @Schema(implementation = ProductDescriptionDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Product with that id does not exist", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(schema = @Schema())})
    })
    @GetMapping("/api/product/{id}")
    public ResponseEntity<ProductDescriptionDTO> getProductById(@PathVariable Long id) {
        try {
            ProductDescriptionDTO product = productService.getProductById(id);
            return ResponseEntity.ok(product);
        } catch (ProductWithThatIdDoesNotExistException e) {
            System.out.println("ERROR: " + e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.out.println("ERROR: " + e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
