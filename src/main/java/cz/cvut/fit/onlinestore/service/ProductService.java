package cz.cvut.fit.onlinestore.service;

import cz.cvut.fit.onlinestore.dao.dto.ProductDescriptionDTO;
import cz.cvut.fit.onlinestore.dao.repository.ProductRepository;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    public Optional<ProductDescriptionDTO> getProductById(Long id) {
        Optional<Tuple> productTuple = productRepository.getProductById(id);

        if (productTuple.isPresent()) {
            Tuple tuple = productTuple.get();
            String name = tuple.get(0, String.class);
            String description = tuple.get(1, String.class);
            Double price = tuple.get(2, Double.class);
            String category = tuple.get(3, String.class);
            String image = tuple.get(4, String.class);

            return Optional.of(new ProductDescriptionDTO(id, name, description, price, category, image));
        }

        return Optional.empty();
    }
}
