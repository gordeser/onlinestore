package cz.cvut.fit.onlinestore.dao.repository;

import cz.cvut.fit.onlinestore.dao.entity.Product;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p.id, p.name, p.description, p.price, p.category, p.image from Product p")
    List<Tuple> getAllProducts();

    @Query("select p.id, p.name, p.description, p.price, p.category, p.image from Product p where p.category = :category")
    List<Tuple> getAllProductsWithCategory(String category);

    @Query("select p.name, p.description, p.price, p.category, p.image from Product p where p.id = :id")
    Optional<Tuple> getProductById(Long id);
}
