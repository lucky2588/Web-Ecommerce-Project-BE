package com.total.webecommerce.mapper;

import com.total.webecommerce.entity.dto.CategoryWeb;
import com.total.webecommerce.entity.Category;
import com.total.webecommerce.respository.OfProduct.ProductRepository;

public class CategoryMapper {
    private static  ProductRepository productRepository;
    public static CategoryWeb CategoryMapper(Category category){
        CategoryWeb categoryWeb = CategoryWeb.builder()
                .id(category.getId())
                .name(category.getName())
                .thumbail(category.getThumbail())
                .nums(productRepository.countByBrand_Id(category.getId()))
                .build();
        return categoryWeb;
    }
}
