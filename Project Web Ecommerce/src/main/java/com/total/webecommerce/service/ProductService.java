package com.total.webecommerce.service;


import com.total.webecommerce.entity.dto.BrandWeb;
import com.total.webecommerce.entity.*;
import com.total.webecommerce.entity.projection.OfUser.ProductInfo;
import com.total.webecommerce.entity.projection.Public.CommentProductInfo;
import com.total.webecommerce.entity.support.NotificationStatus;
import com.total.webecommerce.exception.BadResquestException;
import com.total.webecommerce.exception.NotFoundException;
import com.total.webecommerce.respository.OfAdmin.CategoryRepository;
import com.total.webecommerce.respository.OfAdmin.NotificationRepository;
import com.total.webecommerce.respository.OfAdmin.BrandRepository;
import com.total.webecommerce.respository.OfAdmin.RoleRepository;
import com.total.webecommerce.respository.OfProduct.CommentProductRepository;
import com.total.webecommerce.respository.OfProduct.ProductRepository;
import com.total.webecommerce.respository.OfUser.UserRepository;
import com.total.webecommerce.resquest.OfBlog.CreateBlogResquest;
import com.total.webecommerce.resquest.OfProduct.CommentProductResquest;
import com.total.webecommerce.resquest.OfProduct.CreateProductResquest;
import com.total.webecommerce.security.iCurrentImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final CategoryRepository categoryRepository;
    private final RoleRepository roleRepository;
    private final iCurrentImpl iCurrentUser;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentProductRepository commentProductRepository;
    @Autowired
    private BrandRepository brandRepository;


    public ResponseEntity<?> getProductsBestSeller(Integer page, Integer pageSize) {
        return ResponseEntity.ok(productRepository.getProductOrderByNumsSoldOut(PageRequest.of(page, pageSize)));
    }

    public ResponseEntity<?> getProductsTopView(Integer page, Integer pageSize) {
        return ResponseEntity.ok(productRepository.getProductOrderTopView(PageRequest.of(page, pageSize)));
    }

    public ResponseEntity<?> getProductSales(Integer page, Integer pageSize) {
        return ResponseEntity.ok(productRepository.findBySalesGreaterThan(1.0,PageRequest.of(page, pageSize,Sort.by("discountId").descending())));
    }

    public ResponseEntity<?> getProductNew(Integer page, Integer pageSize) {
        return ResponseEntity.ok(productRepository.getProductNew(PageRequest.of(page, pageSize)));
    }

    // lọc Sp theo Yêu cầu
    public Page<ProductInfo> getProductFilter(Integer brandId, Integer categoryId, Long price,Integer option, Integer page, Integer pageSize) {
        if(brandId == 200 && option == 100){
            return productRepository.getProductFilterAboutCategory(categoryId,price,PageRequest.of(page,pageSize));
        }
        if(brandId == 200 && option == 1){
            return productRepository.getProductFilterAboutCategoryHaveViewHigh(categoryId,price,PageRequest.of(page,pageSize));
        }
        if(brandId == 200 && option == 2){
            return productRepository.getProductFilterAboutCategoryHaveSoldOutHigh(categoryId,price,PageRequest.of(page,pageSize));
        }
        if(brandId == 200 && option == 3){
            return productRepository.getProductFilterAboutCategoryHaveCreateAt(categoryId,price,PageRequest.of(page,pageSize));
        }
        if(brandId != 200 ){
            if(option == 1){
                return productRepository.getProductFilterHaveViewHigh(brandId,categoryId,price,PageRequest.of(page,pageSize));
            }
            if(option == 2){
                return productRepository.getProductFilterHaveNumsSold(brandId,categoryId,price,PageRequest.of(page,pageSize));
            }else{
                return productRepository.getProductFilterHaveCreateAt(brandId,categoryId,price,PageRequest.of(page,pageSize));
            }
        }
        return productRepository.getProductFilter(brandId,categoryId,price,PageRequest.of(page,pageSize));
    }

    public Page<ProductInfo> getProductFilterAboutCategory(Integer categoryId, Long price, Integer page, Integer pageSize) {
        return productRepository.getProductFilterAboutCategory(categoryId,price,PageRequest.of(page,pageSize));
    }

    public List<ProductInfo> getTopProductSeller(){
        List<ProductInfo> productInfos = productRepository.getProductTop4BestSeller();
        return productInfos;
    }

    public List<ProductInfo> getTopProductView(){
        List<ProductInfo> productInfos = productRepository.getProductTop4BestView();
        return productInfos;
    }
    // Lấy ra danh sách Brand có số lượng Cateogry
    public List<BrandWeb> getBrandbyCategory(Integer categoryId){
        List<Brand> brands = brandRepository.findAll();
        return brands.stream().map(e->{
            return new BrandWeb(e.getId(),e.getName(), e.getThumbail(),e.getDescription(), productRepository.findBrandByCategory(categoryId,e.getId()));
        }).toList();
    }

    // Lấy thông tin Product
    public Product getProductById(Integer productId) {
        Optional<ProductInfo> product = productRepository.FindProductById(productId);
        if(product.isEmpty()){
            throw new NotFoundException("Không tìm thấy Product với Id" + productId);
        }
        Product productOriginal = productRepository.findById(productId).orElse(null);
        productOriginal.setView(productOriginal.getView() + 1);
        productRepository.save(productOriginal);
        return productOriginal;
    }
    // Lấy danh sách Cmt của Product
    public Page<CommentProductInfo> getCommentForProduct(Integer page, Integer pageSize,Integer productId){
        return commentProductRepository.findByProduct_IdOrderByIdDesc(productId,PageRequest.of(page,pageSize));
    }


    public List<ProductInfo> getProductSimilar(Integer productId,Integer brandId){
        Random rd = new Random();
        return productRepository.findByBrand_IdAndIdNotIn(brandId,List.of(productId)).subList(0,4);
    }

    public List<ProductInfo> getProductSimilarForBlog(Integer brandId) {
        return productRepository.findByBrand_Id(brandId).subList(0,4);
    }

    // viết gửi comment cho product
    public ResponseEntity<?> sendCommentProduct(Integer productId, CommentProductResquest resquest){
        Product product = productRepository.findById(productId).orElse(null);
        User user = userRepository.findByEmail(resquest.getEmail()).orElse(null);
        CommentProduct cmt = CommentProduct.builder()
                .product(product)
                .user(user)
                .content(resquest.getContentComment())
                .build();
        commentProductRepository.save(cmt);

        Notification notification = Notification.builder()
                .username(user.getName())
                .title("Comment for Product ID "+productId)
                .content(user.getName()+" :  " +resquest.getContentComment())
                .notificationStatus(NotificationStatus.COMMENT)
                .typeOf(0)
                .build();
        notificationRepository.save(notification);
        return ResponseEntity.ok("Cảm ơn bạn đã bình luận về Sản phẩm của chúng tôi !! ");
    }
    public void deleteComment(Integer commentId) { // xóa commnet
        User user = iCurrentUser.getUser();
        CommentProduct cmt = commentProductRepository.findById(commentId).orElseThrow(
                ()-> {
                    throw new NotFoundException("Not found Comment with Id " +commentId);
                }

        );
        Role role = roleRepository.findById(1).orElseThrow(
                ()-> {
                    throw new NotFoundException("Not found Role ");
                }
        );
        if(user.getId() != cmt.getUser().getId() && !user.getRoles().equals(role)){
            throw new BadResquestException("Bạn không có quyền !! ");
        }
        commentProductRepository.delete(cmt);
    }

    public Page<ProductInfo> searchProduct(String title,PageRequest pageRequest) {
        return productRepository.findByContentLikeIgnoreCaseOrNameContainsIgnoreCaseOrBrand_NameLikeIgnoreCaseOrCategory_NameContainsIgnoreCase(title,title,title,title,pageRequest);
    }


    public Integer createProduct(CreateProductResquest resquest) {
        Brand brand = brandRepository.findById(resquest.getBrandId()).orElseThrow(
                ()->{
                    throw new BadResquestException("Not found Brand with ID " +resquest.getBrandId());
                }
        );
        Category category = categoryRepository.findById(resquest.getCategoryId()).orElseThrow(
                ()->{
                    throw new BadResquestException("Not found Category with ID " +resquest.getCategoryId());
                }
        );
        Product item = Product.builder()
                .detail(resquest.getDetail())
                .description(resquest.getDescription())
                .nums(resquest.getNums())
                .content(resquest.getContent())
                .name(resquest.getName())
                .price(resquest.getPrice())
                .brand(brand)
                .category(category)
                .build();
        productRepository.save(item);
        return item.getId();
    }











}
