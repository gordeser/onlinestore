package cz.cvut.fit.onlinestore.service;

import cz.cvut.fit.onlinestore.dao.dto.CommentDescriptionDTO;
import cz.cvut.fit.onlinestore.dao.dto.UsersCommentDTO;
import cz.cvut.fit.onlinestore.dao.entity.Comment;
import cz.cvut.fit.onlinestore.dao.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public List<CommentDescriptionDTO> getCommentsByProductId(Long id) {
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

}
