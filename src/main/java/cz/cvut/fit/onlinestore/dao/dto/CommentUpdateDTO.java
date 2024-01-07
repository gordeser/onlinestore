package cz.cvut.fit.onlinestore.dao.dto;

import java.time.LocalDateTime;

public record CommentUpdateDTO(String text, LocalDateTime date) {
}
