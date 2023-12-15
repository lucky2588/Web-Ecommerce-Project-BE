package com.total.webecommerce.resquest.OfOrder;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddProductToOrder {
    private String email;
    private Integer productId;
      @Min(1)
    private Integer nums;
}
