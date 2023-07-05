package com.total.webecommerce.response;

import com.total.webecommerce.entity.projection.OfUser.FavoritesInfo;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class WishListResponse {
    private Integer check;
    private FavoritesInfo favoritesInfo;

}
