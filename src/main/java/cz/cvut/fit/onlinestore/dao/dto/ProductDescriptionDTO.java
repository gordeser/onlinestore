package cz.cvut.fit.onlinestore.dao.dto;


public record ProductDescriptionDTO(Long id, String name, String description, double price, String category, String image) {
}
