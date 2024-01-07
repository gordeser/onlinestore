package cz.cvut.fit.onlinestore.dao.repository;

import cz.cvut.fit.onlinestore.dao.entity.Comment;
import cz.cvut.fit.onlinestore.dao.entity.Product;
import cz.cvut.fit.onlinestore.dao.entity.Users;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;


@DataJpaTest
public class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private ProductRepository productRepository;

    @Test
    public void getAllCommentsByProductIdPositive() {
        var u1 = new Users();
        var p1 = new Product();
        var c1 = new Comment();

        u1.setId(1L);
        p1.setId(1L);

        c1.setProduct(p1);
        c1.setUsers(u1);
        c1.setId(1L);
        c1.setText("test");

        usersRepository.save(u1);
        productRepository.save(p1);
        commentRepository.save(c1);

        List<Comment> comments = commentRepository.findAllByProductId(p1.getId());
        Assertions.assertFalse(comments.isEmpty());
        Assertions.assertEquals(1, comments.size());

        Comment c = comments.get(0);
        Assertions.assertEquals(c1.getId(), c.getId());
        Assertions.assertEquals(c1.getProduct().getId(), c.getProduct().getId());
        Assertions.assertEquals(c1.getText(), c.getText());
        Assertions.assertEquals(c1.getUsers().getId(), c.getUsers().getId());
    }

    @Test
    public void getAllCommentsByProductIdFalse() {
        var u1 = new Users();
        var p1 = new Product();
        var c1 = new Comment();

        u1.setId(1L);
        p1.setId(1L);

        c1.setProduct(p1);
        c1.setUsers(u1);
        c1.setId(1L);
        c1.setText("test");

        usersRepository.save(u1);
        productRepository.save(p1);
        commentRepository.save(c1);

        List<Comment> comments = commentRepository.findAllByProductId(-1L);
        Assertions.assertTrue(comments.isEmpty());
    }
}
