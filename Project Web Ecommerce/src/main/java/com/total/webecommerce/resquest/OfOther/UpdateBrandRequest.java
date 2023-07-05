package com.total.webecommerce.resquest.OfOther;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class UpdateBrandRequest {
    private Integer brandId;
    @NotEmpty(message = "Đây là trường không được để trống ")
    private String name;
    @NotEmpty(message = "Đây là trường không được để trống ")
    private String thumbail;
    @NotEmpty(message = "Đây là trường không được để trống ")
    private String description;
}