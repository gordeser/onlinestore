package cz.cvut.fit.onlinestore.dao.dto;

import java.time.LocalDateTime;

public record CommentDescriptionDTO(Long id, String text, LocalDateTime date, UsersCommentDTO user) {
}
