package cz.cvut.fit.onlinestore.dao.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @Column(name = "id_comment")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_users")
    private Users users;
    @ManyToOne
    @JoinColumn(name = "id_product")
    private Product product;
    private String text;
    private LocalDateTime date;
}
