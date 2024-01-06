package cz.cvut.fit.onlinestore.dao.repository;

import cz.cvut.fit.onlinestore.dao.entity.Comment;
import cz.cvut.fit.onlinestore.dao.entity.Product;
import cz.cvut.fit.onlinestore.dao.entity.Users;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;


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

        List<Comment> comments = commentRepository.getAllCommentsByProductId(p1.getId());
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

        List<Comment> comments = commentRepository.getAllCommentsByProductId(-1L);
        Assertions.assertTrue(comments.isEmpty());
    }

    @Test
    public void getCommentByIdPositive() {
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

        Optional<Comment> comment = commentRepository.getCommentById(c1.getId());
        Assertions.assertTrue(comment.isPresent());

        Comment c = comment.get();
        Assertions.assertEquals(c1.getId(), c.getId());
        Assertions.assertEquals(c1.getProduct().getId(), c.getProduct().getId());
        Assertions.assertEquals(c1.getText(), c.getText());
        Assertions.assertEquals(c1.getUsers().getId(), c.getUsers().getId());
    }

    @Test
    public void getCommentByIdFalse() {
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

        Optional<Comment> comment = commentRepository.getCommentById(-1L);
        Assertions.assertTrue(comment.isEmpty());
    }

    @Test
    public void updateCommentPositive() {
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

        int comment = commentRepository.updateComment(c1.getId(), "newTest", c1.getDate());
        Assertions.assertEquals(1, comment);
    }

    @Test
    public void updateCommentFalse() {
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

        int comment = commentRepository.updateComment(-1L, "newTest", c1.getDate());
        Assertions.assertEquals(0, comment);
    }

    @Test
    public void getCommentByIdAndProductIdPositive() {
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

        Optional<Comment> comment = commentRepository.getCommentByIdAndProductId(c1.getId(), p1.getId());
        Assertions.assertTrue(comment.isPresent());

        Comment c = comment.get();
        Assertions.assertEquals(c1.getId(), c.getId());
        Assertions.assertEquals(c1.getProduct().getId(), c.getProduct().getId());
        Assertions.assertEquals(c1.getText(), c.getText());
        Assertions.assertEquals(c1.getUsers().getId(), c.getUsers().getId());
    }

    @Test
    public void getCommentByIdAndProductIdFalseCommentId() {
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

        Optional<Comment> comment = commentRepository.getCommentByIdAndProductId(-1L, p1.getId());
        Assertions.assertTrue(comment.isEmpty());
    }

    @Test
    public void getCommentByIdAndProductIdFalseProductId() {
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

        Optional<Comment> comment = commentRepository.getCommentByIdAndProductId(c1.getId(), -1L);
        Assertions.assertTrue(comment.isEmpty());
    }

    @Test
    public void getCommentByIdAndProductIdFalseBoth() {
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

        Optional<Comment> comment = commentRepository.getCommentByIdAndProductId(-1L, -1L);
        Assertions.assertTrue(comment.isEmpty());
    }
}
