package com.total.webecommerce.controller;


import com.total.webecommerce.entity.dto.BrandWeb;
import com.total.webecommerce.entity.dto.CategoryWeb;
import com.total.webecommerce.service.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/public")
public class WebController {
    @Autowired
    private WebService service;


 // Lấy danh sách Category Web
    @GetMapping("getCategories")
    public List<CategoryWeb> getCategories(){
        return service.getCategory();
    }

    @GetMapping("getCategory/{categoryId}")
    public CategoryWeb getCategoryById(@PathVariable Integer categoryId){
        return service.getCategoryById(categoryId);
    }

    @GetMapping("getBrand/{brandId}")
    public BrandWeb getBrandById(@PathVariable Integer brandId){
        return service.getBrandById(brandId);
    }
    // Lấy danh sách Brand Web
    @GetMapping("getBrand")
    public List<BrandWeb> getBrand(){
        return service.getBrand();
    }

    @GetMapping("getBrandformCategory/{categoryId}")
    public List<BrandWeb> getBrandformCategory(@PathVariable Integer categoryId){
        return service.getBrandbyCategory(categoryId);
    }




}
