package cz.cvut.fit.onlinestore.dao.dto;

import cz.cvut.fit.onlinestore.dao.entity.OrderStatus;

import java.time.LocalDateTime;

public record OrdersUpdateDTO(LocalDateTime date, OrderStatus status) {
}
