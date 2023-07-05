package com.total.webecommerce.entity.projection.OfUser;

import com.total.webecommerce.entity.OrderItem;
import lombok.RequiredArgsConstructor;

/**
 * Projection for {@link OrderItem}
 */
public interface OrderItemInfo {
    Integer getId();

    Integer getNums();

    Double getPrice();

    ProductInfo getProduct();
    @RequiredArgsConstructor
    class OrderItemInfoImpl implements OrderItemInfo{
        private final OrderItem item;
        @Override
        public Integer getId() {
            return item.getId();
        }

        @Override
        public Integer getNums() {
            return item.getNums();
        }

        @Override
        public Double getPrice() {
            return item.getPrice();
        }

        @Override
        public ProductInfo getProduct() {
            return ProductInfo.of(item.getProduct());
        }
    }
    static OrderItemInfo of(OrderItem orderItem) {
        return new OrderItemInfoImpl(orderItem);
    }
}