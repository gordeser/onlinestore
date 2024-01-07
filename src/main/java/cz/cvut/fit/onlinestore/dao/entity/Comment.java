package cz.cvut.fit.onlinestore.dao.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Getter
@Setter
public class Comment {
    @Id
    @Column(name = "id_comment")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_users")
    private Users users;
    @ManyToOne
    @JoinColumn(name = "id_product")
    private Product product;
    private String text;
    @CreationTimestamp
    private LocalDateTime date;
}
