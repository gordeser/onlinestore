package cz.cvut.fit.onlinestore.dao.repository;

import cz.cvut.fit.onlinestore.dao.entity.Comment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c where c.product.id = :id")
    List<Comment> getAllCommentsByProductId(Long id);

    Optional<Comment> getCommentById(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Comment c SET c.text = :text, c.date = :date WHERE c.id = :id")
    int updateComment(Long id, String text, LocalDateTime date);
}
