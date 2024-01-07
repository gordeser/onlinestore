package cz.cvut.fit.onlinestore.controller;

import cz.cvut.fit.onlinestore.dao.dto.CommentAddDTO;
import cz.cvut.fit.onlinestore.dao.dto.CommentDescriptionDTO;
import cz.cvut.fit.onlinestore.dao.dto.CommentUpdateDTO;
import cz.cvut.fit.onlinestore.service.CommentService;
import cz.cvut.fit.onlinestore.service.exceptions.CommentDoesNotBelongToThatProductException;
import cz.cvut.fit.onlinestore.service.exceptions.CommentWithThatIdDoesNotExistException;
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
            @ApiResponse(responseCode = "404", description = "Product with that ID does not exist", content = { @Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema())})
    })
    @GetMapping("/api/product/{id}/comment")
    public ResponseEntity<List<CommentDescriptionDTO>> getCommentsByProductId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(commentService.getCommentsByProductId(id));
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
            return ResponseEntity.ok(commentService.addCommentByProductId(id, comment));
        } catch (ProductWithThatIdDoesNotExistException e) {
            System.out.println("ERROR: " + e);
            return ResponseEntity.notFound().build();
        } catch (UserWithThatEmailDoesNotExistException e) {
            System.out.println("ERROR: " + e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            System.out.println("ERROR: " + e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Get comment with id on product id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {@Content(schema = @Schema(implementation = CommentDescriptionDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Comment with that id on product with that id does not exist", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Product with that id does not exist", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(schema = @Schema())})
    })
    @GetMapping("/api/product/{productId}/comment/{commentId}")
    public ResponseEntity<CommentDescriptionDTO> getProductIdCommentById(@PathVariable Long productId, @PathVariable Long commentId) {
        try {
            return ResponseEntity.ok(commentService.getProductIdCommentById(productId, commentId));
        } catch (ProductWithThatIdDoesNotExistException e) {
            return ResponseEntity.notFound().build();
        } catch (CommentWithThatIdDoesNotExistException | CommentDoesNotBelongToThatProductException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Delete comment with id on product id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successful operation", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "Comment with that id on product with that id does not exist", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Product with that id does not exist", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(schema = @Schema())})
    })
    @DeleteMapping("/api/product/{productId}/comment/{commentId}")
    public ResponseEntity<Void> deleteProductIdCommentById(@PathVariable Long productId, @PathVariable Long commentId) {
        try {
            commentService.deleteProductIdCommentById(productId, commentId);
            return ResponseEntity.noContent().build();
        } catch (ProductWithThatIdDoesNotExistException e) {
            return ResponseEntity.notFound().build();
        } catch (CommentWithThatIdDoesNotExistException | CommentDoesNotBelongToThatProductException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Update comment with id on product id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {@Content(schema = @Schema(implementation = CommentDescriptionDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Comment with that id on product with that id does not exist", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Product with that id does not exist", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(schema = @Schema())})
    })
    @PutMapping("/api/product/{productId}/comment/{commentId}")
    public ResponseEntity<CommentDescriptionDTO> updateProductIdCommentById(
            @PathVariable Long productId,
            @PathVariable Long commentId, @RequestBody CommentUpdateDTO commentUpdate) {
        try {
            return ResponseEntity.ok(commentService.updateProductIdCommentById(productId, commentId, commentUpdate));
        } catch (ProductWithThatIdDoesNotExistException e) {
            return ResponseEntity.notFound().build();
        } catch (CommentWithThatIdDoesNotExistException | CommentDoesNotBelongToThatProductException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
