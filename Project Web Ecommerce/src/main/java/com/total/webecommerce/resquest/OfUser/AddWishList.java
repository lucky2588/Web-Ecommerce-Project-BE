package com.total.webecommerce.resquest.OfUser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AddWishList {
    private String email;
    private Integer productId;
}
