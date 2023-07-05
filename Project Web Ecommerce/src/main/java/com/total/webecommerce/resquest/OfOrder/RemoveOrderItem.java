package com.total.webecommerce.resquest.OfOrder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RemoveOrderItem {
    private String email;
    private Integer productId;
}
