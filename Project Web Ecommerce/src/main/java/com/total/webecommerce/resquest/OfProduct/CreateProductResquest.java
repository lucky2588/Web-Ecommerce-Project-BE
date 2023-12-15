package com.total.webecommerce.resquest.OfProduct;

import com.total.webecommerce.entity.Brand;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter

public class CreateProductResquest {
    @NotNull(message = "Không được để trống")
    private String name;
    @NotNull(message = "Không được để trống")
    private String content;
    @NotNull(message = "Không được để trống")
    private String detail;
    @NotNull(message = "Không được để trống")
    private String description;
    @NotNull(message = "Không được để trống")
    private Double price;
    @NotNull(message = "Không được để trống")
    private Integer nums;

    private Integer brandId;
    private Integer categoryId;

}

