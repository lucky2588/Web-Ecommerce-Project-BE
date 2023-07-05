package com.total.webecommerce.entity.projection.OfAdmin;

import com.total.webecommerce.entity.Discount;
import lombok.RequiredArgsConstructor;

/**
 * Projection for {@link Discount}
 */
public interface DiscountInfo {
    Integer getId();

    Integer getPercent();

    @RequiredArgsConstructor
    class DiscountInfoImpl implements DiscountInfo {
        private final Discount discount;

        @Override
        public Integer getId() {
            return discount.getId();
        }

        @Override
        public Integer getPercent() {
            return discount.getPercent();
        }
    }

    static DiscountInfo of(Discount discount) {
        return new DiscountInfoImpl(discount);
    }
}