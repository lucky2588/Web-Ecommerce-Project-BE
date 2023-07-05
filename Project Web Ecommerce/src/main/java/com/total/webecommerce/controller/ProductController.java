package com.total.webecommerce.controller;

import com.total.webecommerce.entity.Product;
import com.total.webecommerce.entity.projection.Public.CommentProductInfo;
import com.total.webecommerce.entity.projection.OfUser.ProductInfo;
import com.total.webecommerce.resquest.OfProduct.CommentProductResquest;
import com.total.webecommerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("api/v1/public")
public class ProductController {
@Autowired
private ProductService service;
    // Các API về Product
    @GetMapping("getProducts/bestSeller") // lấy danh sách SP bán chạy nhất
    public ResponseEntity<?> getProductsBestSeller(@RequestParam(defaultValue = "0") Integer page , @RequestParam(defaultValue = "8") Integer pageSize){
        return service.getProductsBestSeller(page,pageSize);
    }

    @GetMapping("getProducts/viewTop") // lấy danh sách có lượt view Top
    public ResponseEntity<?> getProductViewTop(@RequestParam(defaultValue = "0") Integer page , @RequestParam(defaultValue = "8") Integer pageSize){
        return service.getProductsTopView(page,pageSize);
    }
    @GetMapping("getProducts/ProductSales") // lấy danh sách có lượt view Top
    public ResponseEntity<?> getProductSales(@RequestParam(defaultValue = "0") Integer page , @RequestParam(defaultValue = "8") Integer pageSize){
        return service.getProductSales(page,pageSize);
    }
    @GetMapping("getProducts/newArrival") // lấy danh sách có lượt view Top
    public ResponseEntity<?> getProductNewArrival(@RequestParam(defaultValue = "0") Integer page , @RequestParam(defaultValue = "8") Integer pageSize){
        return service.getProductNew(page,pageSize);
    }
    // Lấy danh sách lọc theo filter
    @GetMapping("getProductsFilter")
    public Page<ProductInfo> getProductsFilter(@RequestParam(required = false) Integer categoryId,
                                               @RequestParam(required = false) Integer brandId,
                                               @RequestParam(required = false) Long price,
                                               @RequestParam Integer option,
                                               @RequestParam(defaultValue = "0") Integer page,
                                               @RequestParam(defaultValue = "5") Integer pageSize
    ) {
        return service.getProductFilter(brandId, categoryId,price,option,page,pageSize);
    }
    @GetMapping("ProductAboutCategory")
    public Page<ProductInfo> getProductsFilterAboutCategory(@RequestParam(required = false) Integer categoryId,
                                                            @RequestParam(required = false) Long price,
                                                            @RequestParam(defaultValue = "0") Integer page,
                                                            @RequestParam(defaultValue = "5") Integer pageSize
    ) {
        return service.getProductFilterAboutCategory(categoryId,price,page,pageSize);
    }

    @GetMapping("getProductTopView")
    public List<ProductInfo> getProductTopview(){
        return service.getTopProductView().subList(0,4);
    }

    @GetMapping("getProductTopSeller")
    public List<ProductInfo> getProductBestSeller(){
        return service.getTopProductSeller().subList(0,4);
    }

    @GetMapping("getProduct/{productId}")
    public Product getProductById(@PathVariable Integer productId){
        return service.getProductById(productId);
    }

    // Lấy danh sách cmt cho product
    @GetMapping("getCommentProduct/{productId}")
    public List<CommentProductInfo> getCommentProduct(@PathVariable Integer productId){
        return service.getCommentForProduct(productId);
    }

    // lấy danh sách sp tương tự cho Blog
    @GetMapping("getProductSimilar/{brandId}")
    public List<ProductInfo> getProductByBrand(@PathVariable Integer brandId){
        return service.getProductSimilarForBlog(brandId);
    }

    @GetMapping("getProductSimilar/{productId}/{brandId}")
    public List<ProductInfo> getProductByBrand(@PathVariable Integer productId,@PathVariable Integer brandId){
        return service.getProductSimilar(productId,brandId);
    }
    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR','USER')")
    // Xử lí gửi comment product gửi lên
    @PostMapping("sendCommentProduct/{productId}")
    public ResponseEntity<?> sendCommentProduct(@PathVariable Integer productId , @Valid @RequestBody CommentProductResquest resquest){
        return service.sendCommentProduct(productId,resquest);
    }

    @GetMapping("getProductForBlog/{brandId}")
    public List<ProductInfo> getProductForBlog(@PathVariable Integer brandId){
        return service.getProductSimilarForBlog(brandId);
    }
    // tìm kiếm SP theo tên
    @GetMapping("searchProduct")
    public Page<ProductInfo> searchProduct(@RequestParam String title,@RequestParam(defaultValue = "0") Integer page,@RequestParam(defaultValue = "8") Integer pageSize ){
        return service.searchProduct(title, PageRequest.of(page,pageSize));
    }

}
