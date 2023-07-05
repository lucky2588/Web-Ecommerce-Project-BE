package com.total.webecommerce.service;

import com.total.webecommerce.entity.*;
import com.total.webecommerce.entity.dto.BestBuyer;
import com.total.webecommerce.entity.projection.OfUser.ManagentUser;
import com.total.webecommerce.entity.projection.OfUser.PaymentInfo;
import com.total.webecommerce.entity.projection.OfUser.UserInfo;
import com.total.webecommerce.entity.support.PaymentStatus;
import com.total.webecommerce.exception.BadResquestException;
import com.total.webecommerce.exception.NotFoundException;
import com.total.webecommerce.response.OverviewInfo;
import com.total.webecommerce.respository.OfAdmin.BrandRepository;
import com.total.webecommerce.respository.OfAdmin.CategoryRepository;
import com.total.webecommerce.respository.OfAdmin.RoleRepository;
import com.total.webecommerce.respository.OfProduct.CommentProductRepository;
import com.total.webecommerce.respository.OfProduct.ProductRepository;
import com.total.webecommerce.respository.OfUser.UserRepository;
import com.total.webecommerce.respository.OrBlog.BlogRepository;
import com.total.webecommerce.respository.OrBlog.CommentOfBlogRepository;
import com.total.webecommerce.respository.OrOrder.OrderBillRepository;
import com.total.webecommerce.respository.OrOrder.OrderItemRepository;
import com.total.webecommerce.respository.OrOrder.PaymentRepository;
import com.total.webecommerce.resquest.OfOther.UpdateBrandRequest;
import com.total.webecommerce.resquest.OfOther.UpdateCategoryRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AdminService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private OrderBillRepository orderBillRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private CommentOfBlogRepository commentOfBlog;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CommentProductRepository commentProductRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    // Info Garrenal
    public OverviewInfo getInfoGarenal() {
        Integer numsPaymentSuccess = paymentRepository.findByPaymentStatus(PaymentStatus.SUCCESS).size();
        Integer numsOrder = paymentRepository.findByPaymentStatus(PaymentStatus.INITIAL).size();
        Integer numsBill = paymentRepository.findByPaymentStatus(PaymentStatus.PROCEED).size();
        List<Payment> listPayment = paymentRepository.getPaymentByStatus(PaymentStatus.SUCCESS);
        double total = 0.0;
        for (int i = 0; i < listPayment.size(); i++) {
            if (listPayment.get(i).getPaymentStatus() == PaymentStatus.SUCCESS) {
                total += listPayment.get(i).getPrice();
            }
        }
        OverviewInfo objInfo = OverviewInfo.builder()
                .numsBlog(blogRepository.findAll().size())
                .numsPayment(numsPaymentSuccess)
                .numsProductBuy(productRepository.findAll().size())
                .numsUser(userRepository.findAll().size())
                .totalSales(total)
                .orderStatus(numsOrder)
                .orderProcess(numsBill)
                .build();
        return objInfo;
    }

    // service for User ...
    // get User

    public UserInfo getUser(String email) {
        return userRepository.findEmailWithInfo(email).orElseThrow(
                () -> {
                    throw new NotFoundException("Không tìm thấy User này !!! ");
                }
        );
    }

    public Page<UserInfo> getPageWithUser(Integer page, Integer pageSize) {
        return userRepository.findAllWithUserInfo(PageRequest.of(page, pageSize));
    }

    public List<BestBuyer> getBestBuyer() {
        return paymentRepository.getBuyer(PaymentStatus.SUCCESS);
    }

    public ResponseEntity<?> uptoBlogerr(Integer userId) {
        log.info("Vào Đây zzz ");
        Role role = roleRepository.findByName("AUTHOR").orElseThrow(
                () -> {
                    throw new NotFoundException("Not Found Role ");
                }
        );
        User user = userRepository.findById(userId).orElseThrow(
                () -> {
                    throw new NotFoundException("Not Found User with ID : " + userId);
                }
        );
        user.getRoles().add(role);
        userRepository.save(user);
        return ResponseEntity.ok("Up to Blogger for User secess !!");
    }

    public ResponseEntity<?> removeBlogger(Integer userId) {
        Role role = roleRepository.findByName("AUTHOR").orElseThrow(
                () -> {
                    throw new NotFoundException("Not Found Role ");
                }
        );
        User user = userRepository.findById(userId).orElseThrow(
                () -> {
                    throw new NotFoundException("Not Found User with ID : " + userId);
                }
        );
        if (user.getRoles().size() == 1) {
            Role roleOfUser = roleRepository.findByName("USER").orElseThrow(
                    () -> {
                        throw new NotFoundException("Not Found Role ");
                    }
            );
            user.getRoles().add(roleOfUser);
            user.getRoles().remove(role);
            userRepository.save(user);



            return ResponseEntity.ok("Remove Blogger Seccess !! ");
        }
        user.getRoles().remove(role);
        userRepository.save(user);
        return ResponseEntity.ok("Remove Blogger Seccess !! ");
    }

    public ResponseEntity<?> disableUser(Integer userId){
        User user = userRepository.findById(userId).orElseThrow(
                () -> {
                    throw new NotFoundException("Not Found User with ID : " + userId);
                }
        );
       if(!paymentRepository.findByUser_IdAndPaymentStatus(userId,PaymentStatus.PROCEED).isEmpty()){
           throw new BadResquestException("This User have Order Bill !! ");
       }
        user.setIsEnable(false);
        userRepository.save(user);
        return ResponseEntity.ok("Disabe User with ID "+userId+" Seccess ");
    }

    public ResponseEntity<?> enableUser(Integer userId){
        User user = userRepository.findById(userId).orElseThrow(
                () -> {
                    throw new NotFoundException("Not Found User with ID : " + userId);
                }
        );
        user.setIsEnable(true);
        userRepository.save(user);
        return ResponseEntity.ok("Enable User with ID "+userId+" Seccess ");
    }


    // service for Category
    public ResponseEntity<?> createCategory(UpdateCategoryRequest request) {
        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .thumbail(request.getThumbail())
                .build();
        categoryRepository.save(category);
        return ResponseEntity.ok("Create Category seccess !! ");
    }

    public ResponseEntity<?> updateCategory(UpdateCategoryRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(
                () -> {
                    throw new NotFoundException("Not found Category ");
                }
        );
        category.setName(request.getName());
        category.setThumbail(request.getThumbail());
        category.setDescription(request.getDescription());
        categoryRepository.save(category);
        return ResponseEntity.ok("Update Category Seccess !! ");
    }

    public ResponseEntity<?> deleteCategory(Integer categoryId) {
        Category acc = categoryRepository.findById(100).get();
        Category item = categoryRepository.findById(categoryId).orElseThrow(
                () -> {
                    throw new NotFoundException("Not found Category with ID" + categoryId);
                }
        );

        List<Product> products = productRepository.findByCategory_Id(categoryId);
        for (Product product : products) {
            product.setCategory(acc);
            productRepository.save(product);
        }
        categoryRepository.delete(item);
        return ResponseEntity.ok("Delete Category Seccesss !! ");
    }

    // Service for Brand
    public ResponseEntity<?> createBrand(UpdateBrandRequest request) {
        Brand brand = Brand.builder()
                .name(request.getName())
                .description(request.getDescription())
                .thumbail(request.getThumbail())
                .build();
        brandRepository.save(brand);
        return ResponseEntity.ok("Create Brand seccess !! ");
    }

    public ResponseEntity<?> updateBrand(UpdateBrandRequest request) {
        Brand item = brandRepository.findById(request.getBrandId()).orElseThrow(
                () -> {
                    throw new NotFoundException("Not found Brand ");
                }
        );
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setThumbail(request.getThumbail());
        brandRepository.save(item);
        return ResponseEntity.ok("Update Brand Seccess !! ");
    }

    public ResponseEntity<?> deleteBrand(Integer brandId) {
        Brand acc = brandRepository.findById(100).get();
        Brand item = brandRepository.findById(brandId).orElseThrow(
                () -> {
                    throw new NotFoundException("Not found Brand with ID" + brandId);
                }
        );
        List<Blog> blogs = blogRepository.findByBrand_Id(brandId);
        List<Product> products = productRepository.removeBrand(brandId);
        for (Product product : products) {
            product.setBrand(acc);
            productRepository.save(product);
        }
        for (Blog blog : blogs) {
            blog.setBrand(acc);
            blogRepository.save(blog);
        }
        brandRepository.delete(item);
        return ResponseEntity.ok("Delete Brand Seccess !! ");
    }

    // service to Order
    public List<PaymentInfo> getPayments() {
        List<PaymentInfo> PaymentList = paymentRepository.findByCreateAt(LocalDate.now().minusDays(3));
        if (PaymentList.isEmpty()) {
            throw new NotFoundException("Not found order this day ");
        }
        return PaymentList;
    }

    public ManagentUser getUserById(Integer userId) {
        Optional<ManagentUser> user = userRepository.findUser(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Not found User with Id" + userId);
        }
        return user.get();
    }


}
