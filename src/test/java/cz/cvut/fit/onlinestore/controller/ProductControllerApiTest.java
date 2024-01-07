package cz.cvut.fit.onlinestore.controller;

import cz.cvut.fit.onlinestore.dao.dto.ProductAddDTO;
import cz.cvut.fit.onlinestore.dao.entity.Product;
import cz.cvut.fit.onlinestore.service.ProductService;
import cz.cvut.fit.onlinestore.service.exceptions.ProductWithThatIdDoesNotExistException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@WebMvcTest(ProductController.class)
public class ProductControllerApiTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductService productService;

    @Test
    public void getAllProducts() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("name");
        product.setDescription("desc");
        product.setPrice(1);
        product.setCategory("cat");
        product.setImage("img");
        Mockito.when(productService.getAllProducts(null)).thenReturn(List.of(product));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/product"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        String.format("[{\"id\":%d,\"name\":\"%s\",\"description\":\"%s\",\"price\":1.0,\"category\":\"%s\",\"image\":\"%s\"}]",
                                product.getId(), product.getName(), product.getDescription(), product.getCategory(), product.getImage()
                        )));
    }

    @Test
    public void getProductById() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("name");
        product.setDescription("desc");
        product.setPrice(1);
        product.setCategory("cat");
        product.setImage("img");

        Mockito.when(productService.getProductById(product.getId())).thenReturn(product);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/product/{id}", product.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        String.format("{\"id\":%d,\"name\":\"%s\",\"description\":\"%s\",\"price\":1.0,\"category\":\"%s\",\"image\":\"%s\"}",
                                product.getId(), product.getName(), product.getDescription(), product.getCategory(), product.getImage()
                        )));
    }

    @Test
    public void getProductByIdWrongId() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("name");
        product.setDescription("desc");
        product.setPrice(1);
        product.setCategory("cat");
        product.setImage("img");

        Mockito.when(productService.getProductById(product.getId())).thenThrow(new ProductWithThatIdDoesNotExistException());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/product/{id}", product.getId()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void createProduct() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("name");
        product.setDescription("desc");
        product.setPrice(1);
        product.setCategory("cat");
        product.setImage("img");

        ProductAddDTO add = new ProductAddDTO(product.getName(), product.getDescription(), product.getPrice(), product.getCategory(), product.getImage());

        Mockito.when(productService.createProduct(add)).thenReturn(product);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/product")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(String.format("{\"name\":\"%s\",\"description\":\"%s\",\"price\":1.0,\"category\":\"%s\",\"image\":\"%s\"}",
                                        add.name(), add.description(), add.category(), add.image()
                                )))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        String.format("{\"id\":%d,\"name\":\"%s\",\"description\":\"%s\",\"price\":1.0,\"category\":\"%s\",\"image\":\"%s\"}",
                                product.getId(), product.getName(), product.getDescription(), product.getCategory(), product.getImage()
                        )));
    }


    @Test
    public void deleteProductById() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("name");
        product.setDescription("desc");
        product.setPrice(1);
        product.setCategory("cat");
        product.setImage("img");
        Mockito.doNothing().when(productService).deleteProductById(product.getId());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/api/product/{id}", product.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }


    @Test
    public void deleteProductByIdWrongId() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("name");
        product.setDescription("desc");
        product.setPrice(1);
        product.setCategory("cat");
        product.setImage("img");
        Mockito.doThrow(new ProductWithThatIdDoesNotExistException()).when(productService).deleteProductById(product.getId());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/api/product/{id}", product.getId()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void updateProduct() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("name");
        product.setDescription("new desc");
        product.setPrice(1);
        product.setCategory("cat");
        product.setImage("img");

        ProductAddDTO update = new ProductAddDTO(product.getName(), "new desc", product.getPrice(), product.getCategory(), product.getImage());

        Mockito.when(productService.updateProductById(product.getId(), update)).thenReturn(product);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/api/product/{id}", product.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(String.format("{\"name\":\"%s\",\"description\":\"%s\",\"price\":1.0,\"category\":\"%s\",\"image\":\"%s\"}",
                                        update.name(), update.description(), update.category(), update.image()
                                )))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        String.format("{\"id\":%d,\"name\":\"%s\",\"description\":\"%s\",\"price\":1.0,\"category\":\"%s\",\"image\":\"%s\"}",
                                product.getId(), product.getName(), product.getDescription(), product.getCategory(), product.getImage()
                        )));
    }

    @Test
    public void updateProductWrongId() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("name");
        product.setDescription("new desc");
        product.setPrice(1);
        product.setCategory("cat");
        product.setImage("img");

        ProductAddDTO update = new ProductAddDTO(product.getName(), "new desc", product.getPrice(), product.getCategory(), product.getImage());

        Mockito.when(productService.updateProductById(product.getId(), update)).thenThrow(new ProductWithThatIdDoesNotExistException());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/api/product/{id}", product.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(String.format("{\"name\":\"%s\",\"description\":\"%s\",\"price\":1.0,\"category\":\"%s\",\"image\":\"%s\"}",
                                        update.name(), update.description(), update.category(), update.image()
                                )))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
