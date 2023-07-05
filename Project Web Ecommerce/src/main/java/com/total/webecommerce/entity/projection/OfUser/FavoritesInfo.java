package com.total.webecommerce.entity.projection.OfUser;

import com.total.webecommerce.entity.Favorites;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Projection for {@link Favorites}
 */
public interface FavoritesInfo {
    Integer getId();
    UserInfo getUser();
    Set<ProductInfo> getProducts();
    @RequiredArgsConstructor
    class FavoritesInfoImpl implements FavoritesInfo{
        private final Favorites favorites;
        @Override
        public Integer getId() {
            return favorites.getId();
        }

        @Override
        public UserInfo getUser() {
            return UserInfo.of(favorites.getUser());
        }

        @Override
        public Set<ProductInfo> getProducts() {
         return favorites.getProducts().stream().map(e->ProductInfo.of(e)).collect(Collectors.toSet());
        }
    }
}