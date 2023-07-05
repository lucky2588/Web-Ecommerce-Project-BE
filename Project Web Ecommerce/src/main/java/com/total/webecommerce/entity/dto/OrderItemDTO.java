package com.total.webecommerce.entity.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
    private Integer productId;
    private Double priceProduct;
    private String thumbail;
    private Integer numsSold;
    private Double totalPrice;
}
