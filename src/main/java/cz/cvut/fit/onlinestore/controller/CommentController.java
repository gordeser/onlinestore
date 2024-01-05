package cz.cvut.fit.onlinestore.controller;

import cz.cvut.fit.onlinestore.dao.dto.CommentAddDTO;
import cz.cvut.fit.onlinestore.dao.dto.CommentDescriptionDTO;
import cz.cvut.fit.onlinestore.service.CommentService;
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

@Tag(name = "Comment", description = "Access to comments section of products")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "Get comments of product with id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation", content = { @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CommentDescriptionDTO.class)))}),
            @ApiResponse(responseCode = "400", description = "Product with that ID does not exist", content = { @Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema())})
    })
    @GetMapping("/api/product/{id}/comment")
    public ResponseEntity<List<CommentDescriptionDTO>> getCommentsByProductId(@PathVariable Long id) {
        try {
            List<CommentDescriptionDTO> comments = commentService.getCommentsByProductId(id);
            return ResponseEntity.ok(comments);
        } catch (ProductWithThatIdDoesNotExistException e) {
            System.out.println("ERROR: " + e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.out.println("ERROR: " + e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Add comment on product id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {@Content(schema = @Schema(implementation = CommentDescriptionDTO.class))}),
            @ApiResponse(responseCode = "400", description = "User with that email does not exist", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Product with that ID does not exist", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(schema = @Schema())})
    })
    @PostMapping("/api/product/{id}/comment")
    public ResponseEntity<CommentDescriptionDTO> addCommentByProductId(@PathVariable Long id, @RequestBody CommentAddDTO comment) {
        try {
            CommentDescriptionDTO commentCreated = commentService.addCommentByProductId(id, comment);
            return ResponseEntity.ok(commentCreated);
        } catch (ProductWithThatIdDoesNotExistException e) {
            return ResponseEntity.notFound().build();
        } catch (UserWithThatEmailDoesNotExistException e) {
            System.out.println("ERROR: " + e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            System.out.println("ERROR: " + e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
