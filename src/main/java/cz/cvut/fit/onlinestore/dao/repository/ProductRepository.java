package cz.cvut.fit.onlinestore.dao.repository;

import cz.cvut.fit.onlinestore.dao.entity.Product;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p.id, p.name, p.description, p.price, p.category, p.image from Product p")
    List<Tuple> getAllProducts();

    @Query("select p.id, p.name, p.description, p.price, p.category, p.image from Product p where p.category = :category")
    List<Tuple> getAllProductsWithCategory(String category);

    @Query("select p.name, p.description, p.price, p.category, p.image from Product p where p.id = :id")
    Optional<Tuple> getProductById(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Product p SET p.name = :name, p.description = :description, p.price = :price, p.category = :category, p.image = :image WHERE p.id = :id")
    int updateProduct(Long id, String name, String description, double price, String category, String image);
}
