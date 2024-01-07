package cz.cvut.fit.onlinestore.service;

import cz.cvut.fit.onlinestore.dao.dto.ProductAddDTO;
import cz.cvut.fit.onlinestore.dao.entity.Product;
import cz.cvut.fit.onlinestore.dao.repository.ProductRepository;
import cz.cvut.fit.onlinestore.service.exceptions.ProductWithThatIdDoesNotExistException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getAllProducts(String category) {
        return (category != null && !category.isEmpty())
                ? productRepository.getAllProductsWithCategory(category)
                : productRepository.findAll();
    }

    public Product getProductById(Long productId) {
        Optional<Product> product = productRepository.findById(productId);

        if (product.isEmpty()) {
            throw new ProductWithThatIdDoesNotExistException();
        }

        return product.get();
    }

    public Product createProduct(ProductAddDTO product) {
        Product newProduct = new Product();
        newProduct.setName(product.name());
        newProduct.setDescription(product.description());
        newProduct.setPrice(product.price());
        newProduct.setCategory(product.category());
        newProduct.setImage(product.image());
        return productRepository.save(newProduct);
    }

    @Transactional
    public void deleteProductById(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new ProductWithThatIdDoesNotExistException();
        }
    }

    @Transactional
    public Product updateProductById(Long productId, ProductAddDTO productUpdate) {
        Optional<Product> product = productRepository.findById(productId);

        if (product.isEmpty()) {
            throw new ProductWithThatIdDoesNotExistException();
        }

        Product updatedProduct = product.get();
        updatedProduct.setName(productUpdate.name());
        updatedProduct.setDescription(productUpdate.description());
        updatedProduct.setPrice(productUpdate.price());
        updatedProduct.setCategory(productUpdate.category());
        updatedProduct.setImage(productUpdate.image());

        return productRepository.save(updatedProduct);
    }
}
