package cz.cvut.fit.onlinestore.dao.repository;

import cz.cvut.fit.onlinestore.dao.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c where c.product.id = :id")
    List<Comment> getAllCommentsByProductId(Long id);
}
