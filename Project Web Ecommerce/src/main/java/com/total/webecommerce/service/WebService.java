package com.total.webecommerce.service;

import com.total.webecommerce.entity.dto.BlogDTO;
import com.total.webecommerce.entity.dto.BrandWeb;
import com.total.webecommerce.entity.dto.CategoryWeb;
import com.total.webecommerce.entity.*;
import com.total.webecommerce.exception.NotFoundException;
import com.total.webecommerce.mapper.BlogMapper;
import com.total.webecommerce.respository.OfAdmin.BrandRepository;
import com.total.webecommerce.respository.OfAdmin.CategoryRepository;
import com.total.webecommerce.respository.OfProduct.ProductRepository;
import com.total.webecommerce.respository.OrBlog.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
@Service
public class WebService {
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private CategoryRepository categoryRepository;



    //      // Lấy danh sách bài Blog tương tự
    public List<BlogDTO> findBrand(Integer blogId, Integer brandId) {
        return blogRepository.findBrand(brandId, blogId).stream().map(blog -> BlogMapper.toBlogDto(blog)).toList();
    }

    // Lấy danh sách về Category
    public List<CategoryWeb> getCategory() {
        return categoryRepository.findAll().stream().map(e -> {
            return new CategoryWeb(e.getId(), e.getName(), e.getThumbail(),e.getDescription(),productRepository.countByCategory_Id(e.getId()));
        }).toList();
    }

    public List<BrandWeb> getBrand() {
        return brandRepository.findAll().stream().map(e -> {
            return new BrandWeb(e.getId(), e.getName(), e.getThumbail(),e.getDescription(), productRepository.countByBrand_Id(e.getId()));
        }).toList();
    }
    public List<BrandWeb> getBrandbyCategory(Integer categoryId){
        List<Brand> brands = brandRepository.findAll();
        return brands.stream().map(e->{
            return new BrandWeb(e.getId(),e.getName(), e.getThumbail(),e.getDescription(), productRepository.findBrandByCategory(categoryId,e.getId()));
        }).toList();
    }

    public CategoryWeb getCategoryById(Integer categoryId){
      Category category = categoryRepository.findById(categoryId).get();
         CategoryWeb item = CategoryWeb.builder()
                 .id(categoryId)
                 .name(category.getName())
                 .thumbail(category.getThumbail())
                 .description(category.getDescription())
                 .nums(productRepository.countByBrand_Id(categoryId))
                 .build();
         return item;
    }


    public BrandWeb getBrandById(Integer brandId) {
        Brand item = brandRepository.findById(brandId).get();
        BrandWeb brand = BrandWeb.builder()
                .id(brandId)
                .name(item.getName())
                .thumbail(item.getThumbail())
                .description(item.getDescription())
                .nums(productRepository.countByBrand_Id(brandId))
                .build();
        return brand;
    }
}
