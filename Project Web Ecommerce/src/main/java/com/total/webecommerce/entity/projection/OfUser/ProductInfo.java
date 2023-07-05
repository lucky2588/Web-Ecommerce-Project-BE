package com.total.webecommerce.entity.projection.OfUser;

import com.total.webecommerce.entity.Product;
import lombok.RequiredArgsConstructor;

/**
 * Projection for {@link com.total.webecommerce.entity.Product}
 */
public interface ProductInfo {
    Integer getId();

    String getName();

    Double getPrice();

    String getThumbail();

    String getContent();

    Integer getNums();

    Integer getView();

    Integer getNumsSold();

    Double getSales();
    @RequiredArgsConstructor
    class ProductInfoImpl implements ProductInfo {
        private final Product product;

        @Override
        public Integer getId() {
            return product.getId();
        }

        @Override
        public String getName() {
            return product.getName();
        }

        @Override
        public Double getPrice() {
            return product.getPrice();
        }

        @Override
        public String getThumbail() {
            return product.getThumbail();
        }

        @Override
        public Integer getNums() {
            return product.getNums();
        }

        @Override
        public Integer getNumsSold() {
            return product.getNumsSold();
        }

        @Override
        public String getContent() {
            return product.getContent();
        }

        @Override
        public Integer getView() {
            return product.getView();
        }

        @Override
        public Double getSales(){
            return product.getSales();
        }
    }

    static ProductInfo of(Product product) {
        return new ProductInfoImpl(product);
    }

}