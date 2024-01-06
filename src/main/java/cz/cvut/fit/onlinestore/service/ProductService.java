package cz.cvut.fit.onlinestore.service;

import cz.cvut.fit.onlinestore.dao.dto.ProductAddDTO;
import cz.cvut.fit.onlinestore.dao.dto.ProductDescriptionDTO;
import cz.cvut.fit.onlinestore.dao.entity.Product;
import cz.cvut.fit.onlinestore.dao.repository.ProductRepository;
import cz.cvut.fit.onlinestore.service.exceptions.ProductWithThatIdDoesNotExistException;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductDescriptionDTO> getAllProducts(String category) {
        List<Tuple> products = (category != null && !category.isEmpty())
                ? productRepository.getAllProductsWithCategory(category)
                : productRepository.getAllProducts();

        return products.stream()
                .map(product -> new ProductDescriptionDTO(
                        product.get(0, Long.class),
                        product.get(1, String.class),
                        product.get(2, String.class),
                        product.get(3, Double.class),
                        product.get(4, String.class),
                        product.get(5, String.class)
                ))
                .collect(Collectors.toList());
    }

    public ProductDescriptionDTO getProductById(Long id) {
        Optional<Tuple> product = productRepository.getProductById(id);

        if (product.isEmpty()) {
            throw new ProductWithThatIdDoesNotExistException();
        }

        String name = product.get().get(0, String.class);
        String description = product.get().get(1, String.class);
        Double price = product.get().get(2, Double.class);
        String category = product.get().get(3, String.class);
        String image = product.get().get(4, String.class);

        return new ProductDescriptionDTO(id, name, description, price, category, image);
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
    public ProductDescriptionDTO updateProductById(Long id, ProductDescriptionDTO productUpdate) {
        int updatedCount = productRepository.updateProduct(
                id,
                productUpdate.name(),
                productUpdate.description(),
                productUpdate.price(),
                productUpdate.category(),
                productUpdate.image()
        );

        if (updatedCount == 0) {
            throw new ProductWithThatIdDoesNotExistException();
        }

        return productUpdate;
    }
}
