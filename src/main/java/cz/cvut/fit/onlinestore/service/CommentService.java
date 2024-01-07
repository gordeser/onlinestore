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

    public List<CommentDescriptionDTO> getCommentsByProductId(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductWithThatIdDoesNotExistException();
        }

        List<Comment> comments = commentRepository.findAllByProductId(productId);

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

    public CommentDescriptionDTO addCommentByProductId(Long productId, CommentAddDTO comment) {
        Optional<Users> user = usersRepository.findByEmail(comment.userEmail());
        Optional<Product> product = productRepository.findById(productId);

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

        Optional<Comment> comment = commentRepository.findById(commentId);

        if (comment.isEmpty()) {
            throw new CommentWithThatIdDoesNotExistException();
        }

        if (!comment.get().getProduct().getId().equals(productId)) {
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

        Optional<Comment> comment = commentRepository.findById(commentId);

        if (comment.isEmpty()) {
            throw new CommentWithThatIdDoesNotExistException();
        }

        if (!comment.get().getProduct().getId().equals(productId)) {
            throw new CommentDoesNotBelongToThatProductException();
        }

        commentRepository.deleteById(commentId);
    }

    @Transactional
    public CommentDescriptionDTO updateProductIdCommentById(Long productId, Long commentId, CommentUpdateDTO commentUpdate) {
        if (!productRepository.existsById(productId)) {
            throw new ProductWithThatIdDoesNotExistException();
        }

        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isEmpty()) {
            throw new CommentWithThatIdDoesNotExistException();
        }

        if (!comment.get().getProduct().getId().equals(productId)) {
            throw new CommentDoesNotBelongToThatProductException();
        }

        Comment updatedComment = comment.get();
        updatedComment.setText(commentUpdate.text());
        updatedComment.setDate(commentUpdate.date());
        commentRepository.save(updatedComment);

        return new CommentDescriptionDTO(
                productId,
                updatedComment.getText(),
                updatedComment.getDate(),
                new UsersCommentDTO(
                        updatedComment.getUsers().getName(),
                        updatedComment.getUsers().getSurname(),
                        updatedComment.getUsers().getEmail()));
    }
}
