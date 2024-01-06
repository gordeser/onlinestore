package cz.cvut.fit.onlinestore.service;

import cz.cvut.fit.onlinestore.dao.dto.CommentAddDTO;
import cz.cvut.fit.onlinestore.dao.dto.CommentDescriptionDTO;
import cz.cvut.fit.onlinestore.dao.dto.CommentUpdateDTO;
import cz.cvut.fit.onlinestore.dao.dto.UsersCommentDTO;
import cz.cvut.fit.onlinestore.dao.entity.Comment;
import cz.cvut.fit.onlinestore.dao.entity.Product;
import cz.cvut.fit.onlinestore.dao.entity.Users;
import cz.cvut.fit.onlinestore.dao.repository.CommentRepository;
import cz.cvut.fit.onlinestore.dao.repository.ProductRepository;
import cz.cvut.fit.onlinestore.dao.repository.UsersRepository;
import cz.cvut.fit.onlinestore.service.exceptions.CommentDoesNotBelongToThatProductException;
import cz.cvut.fit.onlinestore.service.exceptions.CommentWithThatIdDoesNotExistException;
import cz.cvut.fit.onlinestore.service.exceptions.ProductWithThatIdDoesNotExistException;
import cz.cvut.fit.onlinestore.service.exceptions.UserWithThatEmailDoesNotExistException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UsersRepository usersRepository;
    private final ProductRepository productRepository;

    public List<CommentDescriptionDTO> getCommentsByProductId(Long id) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isEmpty()) {
            throw new ProductWithThatIdDoesNotExistException();
        }

        List<Comment> comments = commentRepository.getAllCommentsByProductId(id);

        return comments.stream()
                .map(comment -> new CommentDescriptionDTO(
                        comment.getId(),
                        comment.getText(),
                        comment.getDate(),
                        new UsersCommentDTO(
                                comment.getUsers().getName(),
                                comment.getUsers().getSurname(),
                                comment.getUsers().getEmail()
                        )
                ))
                .collect(Collectors.toList());
    }

    public CommentDescriptionDTO addCommentByProductId(Long id, CommentAddDTO comment) {
        Optional<Users> user = usersRepository.findByEmail(comment.userEmail());
        Optional<Product> product = productRepository.findById(id);

        if (user.isEmpty()) {
            throw new UserWithThatEmailDoesNotExistException();
        }

        if (product.isEmpty()) {
            throw new ProductWithThatIdDoesNotExistException();
        }

        Comment newComment = new Comment();
        newComment.setProduct(product.get());
        newComment.setText(comment.text());
        newComment.setUsers(user.get());
        commentRepository.save(newComment);

        return new CommentDescriptionDTO(
                newComment.getId(),
                newComment.getText(),
                newComment.getDate(),
                new UsersCommentDTO(
                        newComment.getUsers().getName(),
                        newComment.getUsers().getSurname(),
                        newComment.getUsers().getEmail()));

    }

    public CommentDescriptionDTO getProductIdCommentById(Long productId, Long commentId) {

        if (!productRepository.existsById(productId)) {
            throw new ProductWithThatIdDoesNotExistException();
        }


        Optional<Comment> comment = commentRepository.getCommentById(commentId);

        if (comment.isEmpty()) {
            throw new CommentWithThatIdDoesNotExistException();
        }

        Optional<Comment> commentCheck = commentRepository.getCommentByIdAndProductId(commentId, productId);

        if (commentCheck.isEmpty()) {
            throw new CommentDoesNotBelongToThatProductException();
        }

        return new CommentDescriptionDTO(
                comment.get().getId(),
                comment.get().getText(),
                comment.get().getDate(),
                new UsersCommentDTO(
                        comment.get().getUsers().getName(),
                        comment.get().getUsers().getSurname(),
                        comment.get().getUsers().getEmail()
                )
        );
    }

    @Transactional
    public void deleteProductIdCommentById(Long productId, Long commentId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductWithThatIdDoesNotExistException();
        }

        if (!commentRepository.existsById(commentId)) {
            throw new CommentWithThatIdDoesNotExistException();
        }

        Optional<Comment> commentCheck = commentRepository.getCommentByIdAndProductId(commentId, productId);

        if (commentCheck.isEmpty()) {
            throw new CommentDoesNotBelongToThatProductException();
        }

        commentRepository.deleteById(commentId);
    }

    @Transactional
    public CommentUpdateDTO updateProductIdCommentById(Long productId, Long commentId, CommentUpdateDTO commentUpdate) {
        if (!productRepository.existsById(productId)) {
            throw new ProductWithThatIdDoesNotExistException();
        }

        Optional<Comment> commentCheck = commentRepository.getCommentByIdAndProductId(commentId, productId);

        if (commentCheck.isEmpty()) {
            throw new CommentDoesNotBelongToThatProductException();
        }

        int updatedCount = commentRepository.updateComment(
                commentId,
                commentUpdate.text(),
                commentUpdate.date()
        );

        if (updatedCount == 0) {
            throw new ProductWithThatIdDoesNotExistException();
        }

        return commentUpdate;
    }
}
