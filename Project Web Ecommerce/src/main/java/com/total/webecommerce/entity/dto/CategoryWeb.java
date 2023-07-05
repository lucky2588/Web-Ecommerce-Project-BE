package com.total.webecommerce.entity.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryWeb {
    private Integer id;
    private String name;
    private String thumbail;
    private String description;
    private long nums;
}
