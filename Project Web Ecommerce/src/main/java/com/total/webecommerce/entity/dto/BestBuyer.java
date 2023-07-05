package com.total.webecommerce.entity.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BestBuyer {
    private Integer userId;
    private String username;
    private Long countBill;
    private String thumbail;
    private Double getTotal;
}
