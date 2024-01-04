package cz.cvut.fit.onlinestore.controller;

import cz.cvut.fit.onlinestore.dao.dto.CommentDescriptionDTO;
import cz.cvut.fit.onlinestore.dao.dto.ProductDescriptionDTO;
import cz.cvut.fit.onlinestore.service.CommentService;
import cz.cvut.fit.onlinestore.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class CommentController {
    private final CommentService commentService;
    private final ProductService productService;

    @GetMapping("/api/products/{id}/comments")
    public ResponseEntity<List<CommentDescriptionDTO>> getCommentsByProductId(@PathVariable Long id) {
        Optional<ProductDescriptionDTO> product = productService.getProductById(id);

        if (product.isPresent()) {
            List<CommentDescriptionDTO> comments = commentService.getCommentsByProductId(id);
            return ResponseEntity.ok(comments);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
