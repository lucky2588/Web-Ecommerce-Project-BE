package com.total.webecommerce.resquest.OfOrder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ChangeProductFromOrder {
    private Integer productId;
    private Integer itemId;
    private Integer nums;
}
