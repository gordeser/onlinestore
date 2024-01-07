package cz.cvut.fit.onlinestore.service;

import cz.cvut.fit.onlinestore.dao.dto.ProductAddDTO;
import cz.cvut.fit.onlinestore.dao.entity.Product;
import cz.cvut.fit.onlinestore.dao.repository.ProductRepository;
import cz.cvut.fit.onlinestore.service.exceptions.ProductWithThatIdDoesNotExistException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class ProductServiceUnitTest {
    @Autowired
    private ProductService productService;
    @MockBean
    private ProductRepository productRepository;

    @Test
    public void getAllProductsWithoutCategory() {
        Product product = new Product();
        product.setId(1L);
        product.setName("name");
        product.setDescription("desc");
        product.setPrice(1);
        product.setCategory("cat");
        product.setImage("img");

        Mockito.when(productRepository.findAll()).thenReturn(List.of(product));

        List<Product> products = productService.getAllProducts(null);
        Assertions.assertEquals(1, products.size());

        Product p = products.get(0);
        Assertions.assertEquals(product.getId(), p.getId());
        Assertions.assertEquals(product.getName(), p.getName());
        Assertions.assertEquals(product.getDescription(), p.getDescription());
        Assertions.assertEquals(product.getPrice(), p.getPrice());
        Assertions.assertEquals(product.getCategory(), p.getCategory());
        Assertions.assertEquals(product.getImage(), p.getImage());

        Mockito.verify(productRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void getAllProductsWithCategory() {
        Product product = new Product();
        product.setId(1L);
        product.setName("name");
        product.setDescription("desc");
        product.setPrice(1);
        product.setCategory("cat");
        product.setImage("img");
        String category = "cat";

        Mockito.when(productRepository.getAllProductsWithCategory(category)).thenReturn(List.of(product));
        List<Product> products = productService.getAllProducts(category);
        Assertions.assertEquals(1, products.size());

        Product p = products.get(0);
        Assertions.assertEquals(product.getId(), p.getId());
        Assertions.assertEquals(product.getName(), p.getName());
        Assertions.assertEquals(product.getDescription(), p.getDescription());
        Assertions.assertEquals(product.getPrice(), p.getPrice());
        Assertions.assertEquals(product.getCategory(), p.getCategory());
        Assertions.assertEquals(product.getImage(), p.getImage());

        Mockito.verify(productRepository, Mockito.times(1)).getAllProductsWithCategory(category);
    }

    @Test
    public void getAllProductsWithNonExistingCategory() {
        String wrongCategory = "wrongcategory";

        Mockito.when(productRepository.getAllProductsWithCategory(wrongCategory)).thenReturn(List.of());
        List<Product> products = productService.getAllProducts(wrongCategory);
        Assertions.assertEquals(0, products.size());

        Mockito.verify(productRepository, Mockito.times(1)).getAllProductsWithCategory(wrongCategory);
    }

    @Test
    public void getProductById() {
        Product product = new Product();
        product.setId(1L);
        product.setName("name");
        product.setDescription("desc");
        product.setPrice(1);
        product.setCategory("cat");
        product.setImage("img");

        Mockito.when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        Product p = productService.getProductById(product.getId());
        Assertions.assertEquals(product.getId(), p.getId());
        Assertions.assertEquals(product.getName(), p.getName());
        Assertions.assertEquals(product.getDescription(), p.getDescription());
        Assertions.assertEquals(product.getPrice(), p.getPrice());
        Assertions.assertEquals(product.getCategory(), p.getCategory());
        Assertions.assertEquals(product.getImage(), p.getImage());

        Mockito.verify(productRepository, Mockito.times(1)).findById(product.getId());
    }

    @Test
    public void getProductByIdWrongId() {
        Long wrongId = -1L;

        Mockito.when(productRepository.findById(wrongId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ProductWithThatIdDoesNotExistException.class, () -> productService.getProductById(wrongId));
        Mockito.verify(productRepository, Mockito.times(1)).findById(wrongId);
    }

    @Test
    public void createProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setName("name");
        product.setDescription("desc");
        product.setPrice(1);
        product.setCategory("cat");
        product.setImage("img");

        ProductAddDTO add = new ProductAddDTO(
                product.getName(), 
                product.getDescription(), 
                product.getPrice(), 
                product.getCategory(), 
                product.getImage());

        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(product);

        Product p = productService.createProduct(add);
        Assertions.assertEquals(product.getId(), p.getId());
        Assertions.assertEquals(product.getName(), p.getName());
        Assertions.assertEquals(product.getDescription(), p.getDescription());
        Assertions.assertEquals(product.getPrice(), p.getPrice());
        Assertions.assertEquals(product.getCategory(), p.getCategory());
        Assertions.assertEquals(product.getImage(), p.getImage());

        Mockito.verify(productRepository, Mockito.times(1)).save(Mockito.any(Product.class));
    }

    @Test
    public void deleteProductById() {
        Product product = new Product();
        product.setId(1L);
        product.setName("name");
        product.setDescription("desc");
        product.setPrice(1);
        product.setCategory("cat");
        product.setImage("img");

        Mockito.when(productRepository.existsById(product.getId())).thenReturn(true);
        Mockito.doNothing().when(productRepository).deleteById(product.getId());

        productService.deleteProductById(product.getId());

        Mockito.verify(productRepository, Mockito.times(1)).existsById(product.getId());
        Mockito.verify(productRepository, Mockito.times(1)).deleteById(product.getId());
    }

    @Test
    public void deleteProductByIdWrongId() {
        Long wrongId = -1L;

        Mockito.when(productRepository.existsById(wrongId)).thenReturn(false);
        Mockito.doNothing().when(productRepository).deleteById(wrongId);

        Assertions.assertThrows(ProductWithThatIdDoesNotExistException.class, () -> productService.deleteProductById(wrongId));

        Mockito.verify(productRepository, Mockito.times(1)).existsById(wrongId);
        Mockito.verify(productRepository, Mockito.times(0)).deleteById(wrongId);
    }

    @Test
    public void updateProductById() {
        Product product = new Product();
        product.setId(1L);
        product.setName("name");
        product.setDescription("desc");
        product.setPrice(1);
        product.setCategory("new desc");
        product.setImage("img");

        String newDesc = "new desc";

        ProductAddDTO add = new ProductAddDTO(
                product.getName(),
                newDesc,
                product.getPrice(),
                product.getCategory(),
                product.getImage());

        Mockito.when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(product);

        Product p = productService.updateProductById(product.getId(), add);
        Assertions.assertEquals(product.getId(), p.getId());
        Assertions.assertEquals(product.getName(), p.getName());
        Assertions.assertEquals(newDesc, p.getDescription());
        Assertions.assertEquals(product.getPrice(), p.getPrice());
        Assertions.assertEquals(product.getCategory(), p.getCategory());
        Assertions.assertEquals(product.getImage(), p.getImage());

        Mockito.verify(productRepository, Mockito.times(1)).findById(product.getId());
        Mockito.verify(productRepository, Mockito.times(1)).save(Mockito.any(Product.class));
    }

    @Test
    public void updateProductByIdWrongId() {
        Product product = new Product();
        product.setId(1L);
        product.setName("name");
        product.setDescription("desc");
        product.setPrice(1);
        product.setCategory("cat");
        product.setImage("img");

        String newDesc = "new desc";
        Long wrongId = -1L;

        ProductAddDTO add = new ProductAddDTO(
                product.getName(),
                newDesc,
                product.getPrice(),
                product.getCategory(),
                product.getImage());

        Mockito.when(productRepository.findById(wrongId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ProductWithThatIdDoesNotExistException.class, () -> productService.updateProductById(wrongId, add));

        Mockito.verify(productRepository, Mockito.times(1)).findById(wrongId);
    }
}
