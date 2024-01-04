package cz.cvut.fit.onlinestore.service;

import cz.cvut.fit.onlinestore.dao.dto.ProductDescriptionDTO;
import cz.cvut.fit.onlinestore.dao.repository.ProductRepository;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductDescriptionDTO> getAllProducts() {
        List<Tuple> products = productRepository.getAllProducts();
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

    public List<ProductDescriptionDTO> getAllProductsWithCategory(String category) {
        List<Tuple> products = productRepository.getAllProductsWithCategory(category);
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
}
