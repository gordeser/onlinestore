package cz.cvut.fit.onlinestore.dao.repository;

import cz.cvut.fit.onlinestore.dao.entity.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Test
    public void getAllProductsWithCategoryPositive() {
        var p1 = new Product();
        p1.setId(1L);
        p1.setName("test name");
        p1.setCategory("cat1");

        productRepository.save(p1);

        List<Product> products = productRepository.getAllProductsWithCategory("cat1");
        Assertions.assertEquals(1, products.size());

        Product product = products.get(0);
        Assertions.assertEquals(p1.getCategory(), product.getCategory());
        Assertions.assertEquals(p1.getName(), product.getName());
        Assertions.assertEquals(p1.getId(), product.getId());
    }

    @Test
    public void getAllProductsWithCategoryFalse() {
        var p1 = new Product();
        p1.setId(1L);
        p1.setName("test name");
        p1.setCategory("cat1");

        productRepository.save(p1);

        List<Product> products = productRepository.getAllProductsWithCategory("some wrong category");
        Assertions.assertEquals(0, products.size());
    }
}
