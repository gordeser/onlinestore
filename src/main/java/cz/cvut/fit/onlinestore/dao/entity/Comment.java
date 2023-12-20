package cz.cvut.fit.onlinestore.dao.entity;

import java.time.LocalDateTime;

public class Comment {
    private Long id;
    private Users users;
    private Product product;
    private String text;
    private LocalDateTime date;
}
