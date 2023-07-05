package com.total.webecommerce.entity.dto;

import lombok.*;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BrandWeb {
    private Integer id;
    private String name;
    private String thumbail;
    private String description;
    private long nums;
}
