package cz.cvut.fit.onlinestore.service;

import cz.cvut.fit.onlinestore.dao.dto.CommentAddDTO;
import cz.cvut.fit.onlinestore.dao.dto.CommentDescriptionDTO;
import cz.cvut.fit.onlinestore.dao.dto.UsersCommentDTO;
import cz.cvut.fit.onlinestore.dao.entity.Comment;
import cz.cvut.fit.onlinestore.dao.entity.Product;
import cz.cvut.fit.onlinestore.dao.entity.Users;
import cz.cvut.fit.onlinestore.dao.repository.CommentRepository;
import cz.cvut.fit.onlinestore.dao.repository.ProductRepository;
import cz.cvut.fit.onlinestore.dao.repository.UsersRepository;
import cz.cvut.fit.onlinestore.service.exceptions.ProductWithThatIdDoesNotExistException;
import cz.cvut.fit.onlinestore.service.exceptions.UserWithThatEmailDoesNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
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

    @Modifying
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
}
