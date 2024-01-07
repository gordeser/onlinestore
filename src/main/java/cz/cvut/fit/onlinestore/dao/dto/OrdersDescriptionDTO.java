package cz.cvut.fit.onlinestore.dao.dto;

import java.util.List;

public record OrdersDescriptionDTO(String userEmail, List<OrdersProductsDTO> orderProducts) {
}
