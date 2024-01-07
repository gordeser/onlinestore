package cz.cvut.fit.onlinestore.service;

import cz.cvut.fit.onlinestore.dao.dto.CommentAddDTO;
import cz.cvut.fit.onlinestore.dao.dto.CommentDescriptionDTO;
import cz.cvut.fit.onlinestore.dao.dto.CommentUpdateDTO;
import cz.cvut.fit.onlinestore.dao.dto.UsersCommentDTO;
import cz.cvut.fit.onlinestore.dao.entity.Comment;
import cz.cvut.fit.onlinestore.dao.entity.Product;
import cz.cvut.fit.onlinestore.dao.entity.Users;
import cz.cvut.fit.onlinestore.dao.repository.CommentRepository;
import cz.cvut.fit.onlinestore.dao.repository.ProductRepository;
import cz.cvut.fit.onlinestore.dao.repository.UsersRepository;
import cz.cvut.fit.onlinestore.service.exceptions.CommentDoesNotBelongToThatProductException;
import cz.cvut.fit.onlinestore.service.exceptions.CommentWithThatIdDoesNotExistException;
import cz.cvut.fit.onlinestore.service.exceptions.ProductWithThatIdDoesNotExistException;
import cz.cvut.fit.onlinestore.service.exceptions.UserWithThatEmailDoesNotExistException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class CommentServiceUnitTest {
    @Autowired
    private CommentService commentService;
    @MockBean
    private CommentRepository commentRepository;
    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private UsersRepository usersRepository;
    private Users user;
    private Product product;
    private Comment comment;
    private CommentDescriptionDTO desc;

    @BeforeEach
    public void setUp() {
        user = new Users();
        product = new Product();
        comment = new Comment();
        user.setId(1L);
        user.setName("name");
        user.setSurname("new surname");
        user.setEmail("email@email.com");
        user.setAddress("address");
        user.setPassword("password");


        product.setId(1L);
        product.setName("name");
        product.setDescription("desc");
        product.setPrice(1);
        product.setCategory("cat");
        product.setImage("img");

        comment.setId(1L);
        comment.setUsers(user);
        comment.setProduct(product);
        comment.setText("new text");
        comment.setDate(LocalDateTime.MAX);

        desc = new CommentDescriptionDTO(
                comment.getId(),
                comment.getText(),
                comment.getDate(),
                new UsersCommentDTO(user.getName(), user.getSurname(), user.getEmail()));
    }

    @Test
    public void getCommentsByProductId() {
        Mockito.when(productRepository.existsById(product.getId())).thenReturn(true);
        Mockito.when(commentRepository.findAllByProductId(product.getId())).thenReturn(List.of(comment));

        List<CommentDescriptionDTO> c = commentService.getCommentsByProductId(product.getId());
        Assertions.assertEquals(1, c.size());

        CommentDescriptionDTO cc = c.get(0);
        Assertions.assertEquals(comment.getId(), cc.id());
        Assertions.assertEquals(comment.getText(), cc.text());
        Assertions.assertEquals(comment.getDate(), cc.date());
        Assertions.assertEquals(user.getName(), cc.user().name());
        Assertions.assertEquals(user.getSurname(), cc.user().surname());
        Assertions.assertEquals(user.getEmail(), cc.user().email());

        Mockito.verify(productRepository, Mockito.times(1)).existsById(product.getId());
        Mockito.verify(commentRepository, Mockito.times(1)).findAllByProductId(product.getId());
    }

    @Test
    public void getCommentsByProductIdWrongProductId() {
        Long wrongId = -1L;
        Mockito.when(productRepository.existsById(wrongId)).thenReturn(false);

        Assertions.assertThrows(ProductWithThatIdDoesNotExistException.class, () -> commentService.getCommentsByProductId(wrongId));

        Mockito.verify(productRepository, Mockito.times(1)).existsById(wrongId);
        Mockito.verify(commentRepository, Mockito.times(0)).findAllByProductId(wrongId);
    }

    @Test
    public void addCommentByProductId() {
        CommentAddDTO add = new CommentAddDTO(comment.getText(), user.getEmail());

        Mockito.when(usersRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Mockito.when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        Mockito.when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(comment);

        CommentDescriptionDTO c = commentService.addCommentByProductId(product.getId(), add);
        Assertions.assertEquals(comment.getText(), c.text());
        Assertions.assertEquals(user.getName(), c.user().name());
        Assertions.assertEquals(user.getSurname(), c.user().surname());
        Assertions.assertEquals(user.getEmail(), c.user().email());

        Mockito.verify(usersRepository, Mockito.times(1)).findByEmail(user.getEmail());
        Mockito.verify(productRepository, Mockito.times(1)).findById(product.getId());
        Mockito.verify(commentRepository, Mockito.times(1)).save(Mockito.any(Comment.class));
    }

    @Test
    public void addCommentByProductIdWrongProductId() {
        CommentAddDTO add = new CommentAddDTO(comment.getText(), user.getEmail());
        Long wrongProductId = -1L;
        Mockito.when(usersRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Mockito.when(productRepository.findById(wrongProductId)).thenReturn(Optional.empty());
        Mockito.when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(comment);

        Assertions.assertThrows(ProductWithThatIdDoesNotExistException.class, () -> commentService.addCommentByProductId(wrongProductId, add));

        Mockito.verify(usersRepository, Mockito.times(1)).findByEmail(user.getEmail());
        Mockito.verify(productRepository, Mockito.times(1)).findById(wrongProductId);
        Mockito.verify(commentRepository, Mockito.times(0)).save(Mockito.any(Comment.class));
    }

    @Test
    public void addCommentByProductIdWrongEmail() {
        String wrongEmail = "wrong@email.com";
        CommentAddDTO add = new CommentAddDTO(comment.getText(), wrongEmail);
        Mockito.when(usersRepository.findByEmail(wrongEmail)).thenReturn(Optional.empty());
        Mockito.when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        Assertions.assertThrows(UserWithThatEmailDoesNotExistException.class, () -> commentService.addCommentByProductId(product.getId(), add));

        Mockito.verify(usersRepository, Mockito.times(1)).findByEmail(wrongEmail);
        Mockito.verify(productRepository, Mockito.times(1)).findById(product.getId());
    }

    @Test
    public void getProductIdCommentById() {
        Mockito.when(productRepository.existsById(product.getId())).thenReturn(true);
        Mockito.when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));

        CommentDescriptionDTO c = commentService.getProductIdCommentById(product.getId(), comment.getId());
        Assertions.assertEquals(comment.getId(), c.id());
        Assertions.assertEquals(comment.getText(), c.text());
        Assertions.assertEquals(comment.getDate(), c.date());
        Assertions.assertEquals(user.getName(), c.user().name());
        Assertions.assertEquals(user.getSurname(), c.user().surname());
        Assertions.assertEquals(user.getEmail(), c.user().email());

        Mockito.verify(productRepository, Mockito.times(1)).existsById(product.getId());
        Mockito.verify(commentRepository, Mockito.times(1)).findById(comment.getId());
    }

    @Test
    public void getProductIdCommentByIdWrongProductId() {
        Long wrongProductId = -1L;
        Mockito.when(productRepository.existsById(wrongProductId)).thenReturn(false);
        Mockito.when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));

        Assertions.assertThrows(ProductWithThatIdDoesNotExistException.class, () -> commentService.getProductIdCommentById(wrongProductId, comment.getId()));

        Mockito.verify(productRepository, Mockito.times(1)).existsById(wrongProductId);
        Mockito.verify(commentRepository, Mockito.times(0)).findById(comment.getId());
    }

    @Test
    public void getProductIdCommentByIdWrongCommentId() {
        Long wrongCommentId = -1L;
        Mockito.when(productRepository.existsById(product.getId())).thenReturn(true);
        Mockito.when(commentRepository.findById(wrongCommentId)).thenReturn(Optional.empty());

        Assertions.assertThrows(CommentWithThatIdDoesNotExistException.class, () -> commentService.getProductIdCommentById(product.getId(), wrongCommentId));

        Mockito.verify(productRepository, Mockito.times(1)).existsById(product.getId());
        Mockito.verify(commentRepository, Mockito.times(1)).findById(wrongCommentId);
    }

    @Test
    public void getProductIdCommentByIdProductDoesNotBelong() {
        Product newP = new Product();
        newP.setId(2L);
        newP.setName("2");
        newP.setDescription("2");
        newP.setPrice(2);
        newP.setCategory("2");
        newP.setImage("2");

        Mockito.when(productRepository.existsById(newP.getId())).thenReturn(true);
        Mockito.when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));

        Assertions.assertThrows(CommentDoesNotBelongToThatProductException.class, () -> commentService.getProductIdCommentById(newP.getId(), comment.getId()));

        Mockito.verify(productRepository, Mockito.times(1)).existsById(newP.getId());
        Mockito.verify(commentRepository, Mockito.times(1)).findById(comment.getId());
    }

    @Test
    public void deleteProductIdCommentById() {
        Mockito.when(productRepository.existsById(product.getId())).thenReturn(true);
        Mockito.when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
        Mockito.doNothing().when(commentRepository).deleteById(comment.getId());

        commentService.deleteProductIdCommentById(product.getId(), comment.getId());

        Mockito.verify(productRepository, Mockito.times(1)).existsById(product.getId());
        Mockito.verify(commentRepository, Mockito.times(1)).findById(comment.getId());
        Mockito.verify(commentRepository, Mockito.times(1)).deleteById(comment.getId());
    }

    @Test
    public void deleteProductIdCommentByIdWrongProductId() {
        Long wrongProductId = -1L;
        Mockito.when(productRepository.existsById(wrongProductId)).thenReturn(false);
        Mockito.when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
        Mockito.doNothing().when(commentRepository).deleteById(comment.getId());

        Assertions.assertThrows(ProductWithThatIdDoesNotExistException.class, () -> commentService.deleteProductIdCommentById(wrongProductId, comment.getId()));

        Mockito.verify(productRepository, Mockito.times(1)).existsById(wrongProductId);
        Mockito.verify(commentRepository, Mockito.times(0)).findById(comment.getId());
        Mockito.verify(commentRepository, Mockito.times(0)).deleteById(comment.getId());
    }

    @Test
    public void deleteProductIdCommentByIdWrongCommentId() {
        Long wrongCommentId = -1L;
        Mockito.when(productRepository.existsById(product.getId())).thenReturn(true);
        Mockito.when(commentRepository.findById(wrongCommentId)).thenReturn(Optional.empty());
        Mockito.doNothing().when(commentRepository).deleteById(wrongCommentId);

        Assertions.assertThrows(CommentWithThatIdDoesNotExistException.class, () -> commentService.deleteProductIdCommentById(product.getId(), wrongCommentId));

        Mockito.verify(productRepository, Mockito.times(1)).existsById(product.getId());
        Mockito.verify(commentRepository, Mockito.times(1)).findById(wrongCommentId);
        Mockito.verify(commentRepository, Mockito.times(0)).deleteById(wrongCommentId);
    }

    @Test
    public void deleteProductIdCommentByIdProductDoesNotBelong() {
        Product newP = new Product();
        newP.setId(2L);
        newP.setName("2");
        newP.setDescription("2");
        newP.setPrice(2);
        newP.setCategory("2");
        newP.setImage("2");

        Mockito.when(productRepository.existsById(newP.getId())).thenReturn(true);
        Mockito.when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
        Mockito.doNothing().when(commentRepository).deleteById(comment.getId());

        Assertions.assertThrows(CommentDoesNotBelongToThatProductException.class, () -> commentService.deleteProductIdCommentById(newP.getId(), comment.getId()));

        Mockito.verify(productRepository, Mockito.times(1)).existsById(newP.getId());
        Mockito.verify(commentRepository, Mockito.times(1)).findById(comment.getId());
        Mockito.verify(commentRepository, Mockito.times(0)).deleteById(comment.getId());

    }

    @Test
    public void updateProductIdCommentById() {
        String updatedText = "updated Text";
        comment.setText(updatedText);
        CommentUpdateDTO update = new CommentUpdateDTO(updatedText, LocalDateTime.MAX);

        Mockito.when(productRepository.existsById(product.getId())).thenReturn(true);
        Mockito.when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
        Mockito.when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(comment);

        CommentDescriptionDTO c = commentService.updateProductIdCommentById(product.getId(), comment.getId(), update);
        Assertions.assertEquals(comment.getId(), c.id());
        Assertions.assertEquals(updatedText, c.text());
        Assertions.assertEquals(comment.getDate(), c.date());
        Assertions.assertEquals(user.getName(), c.user().name());
        Assertions.assertEquals(user.getSurname(), c.user().surname());
        Assertions.assertEquals(user.getEmail(), c.user().email());

        Mockito.verify(productRepository, Mockito.times(1)).existsById(product.getId());
        Mockito.verify(commentRepository, Mockito.times(1)).findById(comment.getId());
        Mockito.verify(commentRepository, Mockito.times(1)).save(Mockito.any(Comment.class));
    }

    @Test
    public void updateProductIdCommentByIdWrongProductId() {
        Long wrongProductId = -1L;
        String updatedText = "updated Text";
        comment.setText(updatedText);
        CommentUpdateDTO update = new CommentUpdateDTO(updatedText, LocalDateTime.MAX);

        Mockito.when(productRepository.existsById(wrongProductId)).thenReturn(false);
        Mockito.when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
        Mockito.when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(comment);

        Assertions.assertThrows(ProductWithThatIdDoesNotExistException.class, () -> commentService.updateProductIdCommentById(wrongProductId, comment.getId(), update));

        Mockito.verify(productRepository, Mockito.times(1)).existsById(wrongProductId);
        Mockito.verify(commentRepository, Mockito.times(0)).findById(comment.getId());
        Mockito.verify(commentRepository, Mockito.times(0)).save(Mockito.any(Comment.class));
    }

    @Test
    public void updateProductIdCommentByIdWrongCommentId() {
        Long wrongCommentId = -1L;
        String updatedText = "updated Text";
        comment.setText(updatedText);
        CommentUpdateDTO update = new CommentUpdateDTO(updatedText, LocalDateTime.MAX);

        Mockito.when(productRepository.existsById(product.getId())).thenReturn(true);
        Mockito.when(commentRepository.findById(wrongCommentId)).thenReturn(Optional.empty());
        Mockito.when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(comment);

        Assertions.assertThrows(CommentWithThatIdDoesNotExistException.class, () -> commentService.updateProductIdCommentById(product.getId(), wrongCommentId, update));

        Mockito.verify(productRepository, Mockito.times(1)).existsById(product.getId());
        Mockito.verify(commentRepository, Mockito.times(1)).findById(wrongCommentId);
        Mockito.verify(commentRepository, Mockito.times(0)).save(Mockito.any(Comment.class));
    }

    @Test
    public void updateProductIdCommentByIdCommentDoesNotBelong() {
        Product newP = new Product();
        newP.setId(2L);
        newP.setName("2");
        newP.setDescription("2");
        newP.setPrice(2);
        newP.setCategory("2");
        newP.setImage("2");
        String updatedText = "updated Text";
        comment.setText(updatedText);
        CommentUpdateDTO update = new CommentUpdateDTO(updatedText, LocalDateTime.MAX);

        Mockito.when(productRepository.existsById(newP.getId())).thenReturn(true);
        Mockito.when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
        Mockito.when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(comment);

        Assertions.assertThrows(CommentDoesNotBelongToThatProductException.class, () -> commentService.updateProductIdCommentById(newP.getId(), comment.getId(), update));

        Mockito.verify(productRepository, Mockito.times(1)).existsById(newP.getId());
        Mockito.verify(commentRepository, Mockito.times(1)).findById(comment.getId());
        Mockito.verify(commentRepository, Mockito.times(0)).save(Mockito.any(Comment.class));
    }
}
