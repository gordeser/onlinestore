package cz.cvut.fit.onlinestore.controller;

import cz.cvut.fit.onlinestore.dao.dto.CommentAddDTO;
import cz.cvut.fit.onlinestore.dao.dto.CommentDescriptionDTO;
import cz.cvut.fit.onlinestore.dao.dto.CommentUpdateDTO;
import cz.cvut.fit.onlinestore.dao.dto.UsersCommentDTO;
import cz.cvut.fit.onlinestore.dao.entity.Comment;
import cz.cvut.fit.onlinestore.dao.entity.Product;
import cz.cvut.fit.onlinestore.dao.entity.Users;
import cz.cvut.fit.onlinestore.service.CommentService;
import cz.cvut.fit.onlinestore.service.exceptions.CommentDoesNotBelongToThatProductException;
import cz.cvut.fit.onlinestore.service.exceptions.CommentWithThatIdDoesNotExistException;
import cz.cvut.fit.onlinestore.service.exceptions.ProductWithThatIdDoesNotExistException;
import cz.cvut.fit.onlinestore.service.exceptions.UserWithThatEmailDoesNotExistException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;

@WebMvcTest(CommentController.class)
public class CommentsControllerApiTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CommentService commentService;

    @Test
    public void getCommentsByProductId() throws Exception {
        Users user = new Users();
        user.setId(1L);
        user.setName("name");
        user.setSurname("new surname");
        user.setEmail("email@email.com");
        user.setAddress("address");
        user.setPassword("password");

        Product product = new Product();
        product.setId(1L);
        product.setName("name");
        product.setDescription("desc");
        product.setPrice(1);
        product.setCategory("cat");
        product.setImage("img");

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setUsers(user);
        comment.setProduct(product);
        comment.setText("new text");
        comment.setDate(LocalDateTime.MAX);

        CommentDescriptionDTO desc = new CommentDescriptionDTO(
                comment.getId(),
                comment.getText(),
                comment.getDate(),
                new UsersCommentDTO(user.getName(), user.getSurname(), user.getEmail()));

        Mockito.when(commentService.getCommentsByProductId(product.getId())).thenReturn(List.of(desc));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/product/{productId}/comment", product.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        String.format("[{\"id\":%d,\"text\":\"%s\",\"date\":\"%s\",\"user\":{\"name\":\"%s\",\"surname\":\"%s\",\"email\":\"%s\"}}]",
                                comment.getId(), comment.getText(), comment.getDate(), comment.getUsers().getName(), comment.getUsers().getSurname(), comment.getUsers().getEmail()
                        )));
    }

    @Test
    public void getCommentsByProductIdWrongProductId() throws Exception {
        Users user = new Users();
        user.setId(1L);
        user.setName("name");
        user.setSurname("new surname");
        user.setEmail("email@email.com");
        user.setAddress("address");
        user.setPassword("password");

        Product product = new Product();
        product.setId(1L);
        product.setName("name");
        product.setDescription("desc");
        product.setPrice(1);
        product.setCategory("cat");
        product.setImage("img");

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setUsers(user);
        comment.setProduct(product);
        comment.setText("new text");
        comment.setDate(LocalDateTime.MAX);

        CommentDescriptionDTO desc = new CommentDescriptionDTO(
                comment.getId(),
                comment.getText(),
                comment.getDate(),
                new UsersCommentDTO(user.getName(), user.getSurname(), user.getEmail()));

        Mockito.when(commentService.getCommentsByProductId(product.getId())).thenThrow(new ProductWithThatIdDoesNotExistException());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/product/{productId}/comment", product.getId()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void addCommentByProductId() throws Exception {
        Users user = new Users();
        user.setId(1L);
        user.setName("name");
        user.setSurname("new surname");
        user.setEmail("email@email.com");
        user.setAddress("address");
        user.setPassword("password");

        Product product = new Product();
        product.setId(1L);
        product.setName("name");
        product.setDescription("desc");
        product.setPrice(1);
        product.setCategory("cat");
        product.setImage("img");

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setUsers(user);
        comment.setProduct(product);
        comment.setText("new text");
        comment.setDate(LocalDateTime.MAX);

        CommentDescriptionDTO desc = new CommentDescriptionDTO(
                comment.getId(),
                comment.getText(),
                comment.getDate(),
                new UsersCommentDTO(user.getName(), user.getSurname(), user.getEmail()));

        CommentAddDTO add = new CommentAddDTO(comment.getText(), user.getEmail());

        Mockito.when(commentService.addCommentByProductId(product.getId(), add)).thenReturn(desc);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/product/{productId}/comment", product.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(String.format("{\"text\":\"%s\",\"userEmail\":\"%s\"}",
                                        desc.text(), user.getEmail()
                                )))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        String.format("{\"id\":%d,\"text\":\"%s\",\"date\":\"%s\",\"user\":{\"name\":\"%s\",\"surname\":\"%s\",\"email\":\"%s\"}}",
                                comment.getId(), comment.getText(), comment.getDate(), comment.getUsers().getName(), comment.getUsers().getSurname(), comment.getUsers().getEmail()
                        )));
    }

    @Test
    public void addCommentByProductIdWrongProduct() throws Exception {
        Users user = new Users();
        user.setId(1L);
        user.setName("name");
        user.setSurname("new surname");
        user.setEmail("email@email.com");
        user.setAddress("address");
        user.setPassword("password");

        Product product = new Product();
        product.setId(1L);
        product.setName("name");
        product.setDescription("desc");
        product.setPrice(1);
        product.setCategory("cat");
        product.setImage("img");

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setUsers(user);
        comment.setProduct(product);
        comment.setText("new text");
        comment.setDate(LocalDateTime.MAX);

        CommentDescriptionDTO desc = new CommentDescriptionDTO(
                comment.getId(),
                comment.getText(),
                comment.getDate(),
                new UsersCommentDTO(user.getName(), user.getSurname(), user.getEmail()));

        CommentAddDTO add = new CommentAddDTO(comment.getText(), user.getEmail());

        Mockito.when(commentService.addCommentByProductId(product.getId(), add)).thenThrow(new ProductWithThatIdDoesNotExistException());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/product/{productId}/comment", product.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(String.format("{\"text\":\"%s\",\"userEmail\":\"%s\"}",
                                        desc.text(), user.getEmail()
                                )))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void addCommentByProductIdWrongUser() throws Exception {
        Users user = new Users();
        user.setId(1L);
        user.setName("name");
        user.setSurname("new surname");
        user.setEmail("email@email.com");
        user.setAddress("address");
        user.setPassword("password");

        Product product = new Product();
        product.setId(1L);
        product.setName("name");
        product.setDescription("desc");
        product.setPrice(1);
        product.setCategory("cat");
        product.setImage("img");

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setUsers(user);
        comment.setProduct(product);
        comment.setText("new text");
        comment.setDate(LocalDateTime.MAX);

        CommentDescriptionDTO desc = new CommentDescriptionDTO(
                comment.getId(),
                comment.getText(),
                comment.getDate(),
                new UsersCommentDTO(user.getName(), user.getSurname(), user.getEmail()));

        CommentAddDTO add = new CommentAddDTO(comment.getText(), user.getEmail());

        Mockito.when(commentService.addCommentByProductId(product.getId(), add)).thenThrow(new UserWithThatEmailDoesNotExistException());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/product/{productId}/comment", product.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(String.format("{\"text\":\"%s\",\"userEmail\":\"%s\"}",
                                        desc.text(), user.getEmail()
                                )))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    public void getProductIdCommentById() throws Exception {
        Users user = new Users();
        user.setId(1L);
        user.setName("name");
        user.setSurname("new surname");
        user.setEmail("email@email.com");
        user.setAddress("address");
        user.setPassword("password");

        Product product = new Product();
        product.setId(1L);
        product.setName("name");
        product.setDescription("desc");
        product.setPrice(1);
        product.setCategory("cat");
        product.setImage("img");

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setUsers(user);
        comment.setProduct(product);
        comment.setText("new text");
        comment.setDate(LocalDateTime.MAX);

        CommentDescriptionDTO desc = new CommentDescriptionDTO(
                comment.getId(),
                comment.getText(),
                comment.getDate(),
                new UsersCommentDTO(user.getName(), user.getSurname(), user.getEmail()));

        Mockito.when(commentService.getProductIdCommentById(product.getId(), comment.getId())).thenReturn(desc);


        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/product/{productId}/comment/{commentId}", product.getId(), comment.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        String.format("{\"id\":%d,\"text\":\"%s\",\"date\":\"%s\",\"user\":{\"name\":\"%s\",\"surname\":\"%s\",\"email\":\"%s\"}}",
                                comment.getId(), comment.getText(), comment.getDate(), comment.getUsers().getName(), comment.getUsers().getSurname(), comment.getUsers().getEmail()
                        )));
    }

    @Test
    public void getProductIdCommentByIdWrongProduct() throws Exception {
        Users user = new Users();
        user.setId(1L);
        user.setName("name");
        user.setSurname("new surname");
        user.setEmail("email@email.com");
        user.setAddress("address");
        user.setPassword("password");

        Product product = new Product();
        product.setId(1L);
        product.setName("name");
        product.setDescription("desc");
        product.setPrice(1);
        product.setCategory("cat");
        product.setImage("img");

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setUsers(user);
        comment.setProduct(product);
        comment.setText("new text");
        comment.setDate(LocalDateTime.MAX);

        CommentDescriptionDTO desc = new CommentDescriptionDTO(
                comment.getId(),
                comment.getText(),
                comment.getDate(),
                new UsersCommentDTO(user.getName(), user.getSurname(), user.getEmail()));

        Mockito.when(commentService.getProductIdCommentById(product.getId(), comment.getId())).thenThrow(new ProductWithThatIdDoesNotExistException());


        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/product/{productId}/comment/{commentId}", product.getId(), comment.getId()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getProductIdCommentByIdWrongCommentId() throws Exception {
        Users user = new Users();
        user.setId(1L);
        user.setName("name");
        user.setSurname("new surname");
        user.setEmail("email@email.com");
        user.setAddress("address");
        user.setPassword("password");

        Product product = new Product();
        product.setId(1L);
        product.setName("name");
        product.setDescription("desc");
        product.setPrice(1);
        product.setCategory("cat");
        product.setImage("img");

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setUsers(user);
        comment.setProduct(product);
        comment.setText("new text");
        comment.setDate(LocalDateTime.MAX);

        CommentDescriptionDTO desc = new CommentDescriptionDTO(
                comment.getId(),
                comment.getText(),
                comment.getDate(),
                new UsersCommentDTO(user.getName(), user.getSurname(), user.getEmail()));

        Mockito.when(commentService.getProductIdCommentById(product.getId(), comment.getId())).thenThrow(new CommentWithThatIdDoesNotExistException());


        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/product/{productId}/comment/{commentId}", product.getId(), comment.getId()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void getProductIdCommentByIdDontBelong() throws Exception {
        Users user = new Users();
        user.setId(1L);
        user.setName("name");
        user.setSurname("new surname");
        user.setEmail("email@email.com");
        user.setAddress("address");
        user.setPassword("password");

        Product product = new Product();
        product.setId(1L);
        product.setName("name");
        product.setDescription("desc");
        product.setPrice(1);
        product.setCategory("cat");
        product.setImage("img");

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setUsers(user);
        comment.setProduct(product);
        comment.setText("new text");
        comment.setDate(LocalDateTime.MAX);

        CommentDescriptionDTO desc = new CommentDescriptionDTO(
                comment.getId(),
                comment.getText(),
                comment.getDate(),
                new UsersCommentDTO(user.getName(), user.getSurname(), user.getEmail()));

        Mockito.when(commentService.getProductIdCommentById(product.getId(), comment.getId())).thenThrow(new CommentDoesNotBelongToThatProductException());


        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/product/{productId}/comment/{commentId}", product.getId(), comment.getId()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void deleteProductIdCommentById() throws Exception {
        Users user = new Users();
        user.setId(1L);
        user.setName("name");
        user.setSurname("new surname");
        user.setEmail("email@email.com");
        user.setAddress("address");
        user.setPassword("password");

        Product product = new Product();
        product.setId(1L);
        product.setName("name");
        product.setDescription("desc");
        product.setPrice(1);
        product.setCategory("cat");
        product.setImage("img");

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setUsers(user);
        comment.setProduct(product);
        comment.setText("new text");
        comment.setDate(LocalDateTime.MAX);

        Mockito.doNothing().when(commentService).deleteProductIdCommentById(product.getId(), comment.getId());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/api/product/{productId}/comment/{commentId}", product.getId(), comment.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void deleteProductIdCommentByIdWrongProduct() throws Exception {
        Users user = new Users();
        user.setId(1L);
        user.setName("name");
        user.setSurname("new surname");
        user.setEmail("email@email.com");
        user.setAddress("address");
        user.setPassword("password");

        Product product = new Product();
        product.setId(1L);
        product.setName("name");
        product.setDescription("desc");
        product.setPrice(1);
        product.setCategory("cat");
        product.setImage("img");

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setUsers(user);
        comment.setProduct(product);
        comment.setText("new text");
        comment.setDate(LocalDateTime.MAX);

        Mockito.doThrow(new ProductWithThatIdDoesNotExistException()).when(commentService).deleteProductIdCommentById(product.getId(), comment.getId());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/api/product/{productId}/comment/{commentId}", product.getId(), comment.getId()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void deleteProductIdCommentByIdWrongCommentId() throws Exception {
        Users user = new Users();
        user.setId(1L);
        user.setName("name");
        user.setSurname("new surname");
        user.setEmail("email@email.com");
        user.setAddress("address");
        user.setPassword("password");

        Product product = new Product();
        product.setId(1L);
        product.setName("name");
        product.setDescription("desc");
        product.setPrice(1);
        product.setCategory("cat");
        product.setImage("img");

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setUsers(user);
        comment.setProduct(product);
        comment.setText("new text");
        comment.setDate(LocalDateTime.MAX);

        Mockito.doThrow(new CommentWithThatIdDoesNotExistException()).when(commentService).deleteProductIdCommentById(product.getId(), comment.getId());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/api/product/{productId}/comment/{commentId}", product.getId(), comment.getId()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void deleteProductIdCommentByIdDontBelong() throws Exception {
        Users user = new Users();
        user.setId(1L);
        user.setName("name");
        user.setSurname("new surname");
        user.setEmail("email@email.com");
        user.setAddress("address");
        user.setPassword("password");

        Product product = new Product();
        product.setId(1L);
        product.setName("name");
        product.setDescription("desc");
        product.setPrice(1);
        product.setCategory("cat");
        product.setImage("img");

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setUsers(user);
        comment.setProduct(product);
        comment.setText("new text");
        comment.setDate(LocalDateTime.MAX);

        Mockito.doThrow(new CommentDoesNotBelongToThatProductException()).when(commentService).deleteProductIdCommentById(product.getId(), comment.getId());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/api/product/{productId}/comment/{commentId}", product.getId(), comment.getId()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void updateProductIdCommentById() throws Exception {
        Users user = new Users();
        user.setId(1L);
        user.setName("name");
        user.setSurname("new surname");
        user.setEmail("email@email.com");
        user.setAddress("address");
        user.setPassword("password");

        Product product = new Product();
        product.setId(1L);
        product.setName("name");
        product.setDescription("desc");
        product.setPrice(1);
        product.setCategory("cat");
        product.setImage("img");

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setUsers(user);
        comment.setProduct(product);
        comment.setText("updated");
        comment.setDate(LocalDateTime.MAX);

        CommentDescriptionDTO desc = new CommentDescriptionDTO(
                comment.getId(),
                comment.getText(),
                comment.getDate(),
                new UsersCommentDTO(user.getName(), user.getSurname(), user.getEmail()));

        CommentUpdateDTO update = new CommentUpdateDTO(comment.getText(), comment.getDate());

        Mockito.when(commentService.updateProductIdCommentById(product.getId(), comment.getId(), update)).thenReturn(desc);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/api/product/{productId}/comment/{commentId}", product.getId(), comment.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(String.format("{\"text\":\"%s\",\"date\":\"%s\"}",
                                        desc.text(), desc.date()
                                )))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        String.format("{\"id\":%d,\"text\":\"%s\",\"date\":\"%s\",\"user\":{\"name\":\"%s\",\"surname\":\"%s\",\"email\":\"%s\"}}",
                                comment.getId(), comment.getText(), comment.getDate(), comment.getUsers().getName(), comment.getUsers().getSurname(), comment.getUsers().getEmail()
                        )));
    }

    @Test
    public void updateProductIdCommentByIdWrongProduct() throws Exception {
        Users user = new Users();
        user.setId(1L);
        user.setName("name");
        user.setSurname("new surname");
        user.setEmail("email@email.com");
        user.setAddress("address");
        user.setPassword("password");

        Product product = new Product();
        product.setId(1L);
        product.setName("name");
        product.setDescription("desc");
        product.setPrice(1);
        product.setCategory("cat");
        product.setImage("img");

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setUsers(user);
        comment.setProduct(product);
        comment.setText("updated");
        comment.setDate(LocalDateTime.MAX);

        CommentDescriptionDTO desc = new CommentDescriptionDTO(
                comment.getId(),
                comment.getText(),
                comment.getDate(),
                new UsersCommentDTO(user.getName(), user.getSurname(), user.getEmail()));

        CommentUpdateDTO update = new CommentUpdateDTO(comment.getText(), comment.getDate());

        Mockito.when(commentService.updateProductIdCommentById(product.getId(), comment.getId(), update)).thenThrow(new ProductWithThatIdDoesNotExistException());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/api/product/{productId}/comment/{commentId}", product.getId(), comment.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(String.format("{\"text\":\"%s\",\"date\":\"%s\"}",
                                        desc.text(), desc.date()
                                )))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void updateProductIdCommentByIdWrongComment() throws Exception {
        Users user = new Users();
        user.setId(1L);
        user.setName("name");
        user.setSurname("new surname");
        user.setEmail("email@email.com");
        user.setAddress("address");
        user.setPassword("password");

        Product product = new Product();
        product.setId(1L);
        product.setName("name");
        product.setDescription("desc");
        product.setPrice(1);
        product.setCategory("cat");
        product.setImage("img");

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setUsers(user);
        comment.setProduct(product);
        comment.setText("updated");
        comment.setDate(LocalDateTime.MAX);

        CommentDescriptionDTO desc = new CommentDescriptionDTO(
                comment.getId(),
                comment.getText(),
                comment.getDate(),
                new UsersCommentDTO(user.getName(), user.getSurname(), user.getEmail()));

        CommentUpdateDTO update = new CommentUpdateDTO(comment.getText(), comment.getDate());

        Mockito.when(commentService.updateProductIdCommentById(product.getId(), comment.getId(), update)).thenThrow(new CommentWithThatIdDoesNotExistException());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/api/product/{productId}/comment/{commentId}", product.getId(), comment.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(String.format("{\"text\":\"%s\",\"date\":\"%s\"}",
                                        desc.text(), desc.date()
                                )))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void updateProductIdCommentByIdDontBelong() throws Exception {
        Users user = new Users();
        user.setId(1L);
        user.setName("name");
        user.setSurname("new surname");
        user.setEmail("email@email.com");
        user.setAddress("address");
        user.setPassword("password");

        Product product = new Product();
        product.setId(1L);
        product.setName("name");
        product.setDescription("desc");
        product.setPrice(1);
        product.setCategory("cat");
        product.setImage("img");

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setUsers(user);
        comment.setProduct(product);
        comment.setText("updated");
        comment.setDate(LocalDateTime.MAX);

        CommentDescriptionDTO desc = new CommentDescriptionDTO(
                comment.getId(),
                comment.getText(),
                comment.getDate(),
                new UsersCommentDTO(user.getName(), user.getSurname(), user.getEmail()));

        CommentUpdateDTO update = new CommentUpdateDTO(comment.getText(), comment.getDate());

        Mockito.when(commentService.updateProductIdCommentById(product.getId(), comment.getId(), update)).thenThrow(new CommentDoesNotBelongToThatProductException());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/api/product/{productId}/comment/{commentId}", product.getId(), comment.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(String.format("{\"text\":\"%s\",\"date\":\"%s\"}",
                                        desc.text(), desc.date()
                                )))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
