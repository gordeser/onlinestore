package cz.cvut.fit.onlinestore.dao.dto;

import java.time.LocalDateTime;

public record CommentUpdateDTO(Long id, String text, LocalDateTime date) {
}
