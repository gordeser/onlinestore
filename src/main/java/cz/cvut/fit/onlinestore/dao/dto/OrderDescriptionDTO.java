package cz.cvut.fit.onlinestore.dao.dto;

import java.util.List;

public record OrderDescriptionDTO(String userEmail, List<OrderProductsDTO> orderProducts) {
}
