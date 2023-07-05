package com.total.webecommerce.entity.projection.Public;

import com.total.webecommerce.entity.Brand;
import lombok.RequiredArgsConstructor;

/**
 * Projection for {@link com.total.webecommerce.entity.Brand}
 */
public interface BrandInfo {
    Integer getId();

    String getName();
    String getDescription();
    String getThumbail();

    @RequiredArgsConstructor
    class BrandInfoImpl implements BrandInfo {

        private final Brand brand;

        @Override
        public Integer getId() {
            return brand.getId();
        }

        @Override
        public String getName() {
            return brand.getName();
        }
        @Override
        public String getDescription() {
            return brand.getDescription();
        }

        @Override
        public String getThumbail(){
            return brand.getThumbail();
        }
    }

    static BrandInfo of(Brand brand) {
        return new BrandInfoImpl(brand);
    }


}