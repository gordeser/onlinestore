package cz.cvut.fit.onlinestore.controller;

import cz.cvut.fit.onlinestore.dao.dto.CommentAddDTO;
import cz.cvut.fit.onlinestore.dao.dto.CommentDescriptionDTO;
import cz.cvut.fit.onlinestore.service.CommentService;
import cz.cvut.fit.onlinestore.service.exceptions.ProductWithThatIdDoesNotExistException;
import cz.cvut.fit.onlinestore.service.exceptions.UserWithThatEmailDoesNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/api/products/{id}/comments")
    public ResponseEntity<List<CommentDescriptionDTO>> getCommentsByProductId(@PathVariable Long id) {
        try {
            List<CommentDescriptionDTO> comments = commentService.getCommentsByProductId(id);
            return ResponseEntity.ok(comments);
        } catch (ProductWithThatIdDoesNotExistException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/api/products/{id}/comments")
    public ResponseEntity<CommentDescriptionDTO> addCommentByProductId(@PathVariable Long id, @RequestBody CommentAddDTO comment) {
        try {
            CommentDescriptionDTO commentCreated = commentService.addCommentByProductId(id, comment);
            return ResponseEntity.ok(commentCreated);
        } catch (UserWithThatEmailDoesNotExistException | ProductWithThatIdDoesNotExistException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
