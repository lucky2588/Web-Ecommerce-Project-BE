package com.total.webecommerce.entity.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductBestSeller {
    private Integer id;
    private Double totalPrice;
    private Integer numsSold;
    private String thumbail;

}
