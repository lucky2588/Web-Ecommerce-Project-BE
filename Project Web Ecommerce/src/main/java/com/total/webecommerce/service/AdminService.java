package com.total.webecommerce.service;

import com.total.webecommerce.entity.*;
import com.total.webecommerce.entity.dto.BestBuyer;
import com.total.webecommerce.entity.projection.OfAdmin.NotificationInfo;
import com.total.webecommerce.entity.projection.OfUser.ManagentUser;
import com.total.webecommerce.entity.projection.OfUser.PaymentInfo;
import com.total.webecommerce.entity.projection.OfUser.UserInfo;
import com.total.webecommerce.entity.projection.Public.BlogInfo;
import com.total.webecommerce.entity.support.ChartJ;
import com.total.webecommerce.entity.support.NotificationStatus;
import com.total.webecommerce.entity.support.PaymentStatus;
import com.total.webecommerce.exception.BadResquestException;
import com.total.webecommerce.exception.NotFoundException;
import com.total.webecommerce.response.ChartjResponse;
import com.total.webecommerce.response.OverviewInfo;
import com.total.webecommerce.respository.OfAdmin.NotificationRepository;
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
import com.total.webecommerce.security.iCurrent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AdminService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private NotificationRepository notificationRepository;
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
    @Autowired
    private iCurrent iCurrent;

    // Info Garrenal
    public OverviewInfo getInfoGarenal() {
        Integer numsPaymentSuccess = paymentRepository.findByPaymentStatus(PaymentStatus.SUCCESS).size();
        Integer numsOrder = paymentRepository.findAll().size();
        Integer numsBill = paymentRepository.findByPaymentStatus(PaymentStatus.INITIAL).size();
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

    public List<BlogInfo> getBlogs() {
        return blogRepository.findBlogsNew().subList(0, 5);
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
        User accOfAdmin = iCurrent.getUser();
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
        Notification notification = Notification.builder()
                .username(accOfAdmin.getName())
                .title("Up to Blogger")
                .content(accOfAdmin.getName() + " up to Blogger for User " + user.getName())
                .avatar(accOfAdmin.getAvatar())
                .notificationStatus(NotificationStatus.ACCOUNT)
                .typeOf(1)
                .build();
        notificationRepository.save(notification);
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
            User accOfAdmin = iCurrent.getUser();
            Notification notification = Notification.builder()
                    .username(accOfAdmin.getName())
                    .title("Remove Blogger")
                    .avatar(accOfAdmin.getAvatar())
                    .content(accOfAdmin.getName() + " remove Blogger of User " + user.getName())
                    .notificationStatus(NotificationStatus.ACCOUNT)
                    .typeOf(1)
                    .build();
            notificationRepository.save(notification);
            user.getRoles().add(roleOfUser);
            user.getRoles().remove(role);
            userRepository.save(user);
            return ResponseEntity.ok("Remove Blogger Seccess !! ");
        }
        User accOfAdmin = iCurrent.getUser();
        Notification notification = Notification.builder()
                .username(accOfAdmin.getName())
                .title("Remove Blogger")
                .content(accOfAdmin.getName() + " remove Blogger of User " + user.getName())
                .notificationStatus(NotificationStatus.ACCOUNT)
                .typeOf(1)
                .build();
        notificationRepository.save(notification);
        user.getRoles().remove(role);
        userRepository.save(user);
        return ResponseEntity.ok("Remove Blogger Seccess !! ");
    }

    public ResponseEntity<?> disableUser(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> {
                    throw new NotFoundException("Not Found User with ID : " + userId);
                }
        );
        if (!paymentRepository.findByUser_IdAndPaymentStatus(userId, PaymentStatus.PROCEED).isEmpty()) {
            throw new BadResquestException("This User have Order Bill !! ");
        }
        user.setIsEnable(false);
        userRepository.save(user);
        User accOfAdmin = iCurrent.getUser();
        Notification notification = Notification.builder()
                .username(accOfAdmin.getName())
                .title("Disable User")
                .avatar(accOfAdmin.getAvatar())
                .content(accOfAdmin.getName() + " Disable User with ID " + user.getId())
                .notificationStatus(NotificationStatus.ACCOUNT)
                .typeOf(1)
                .build();
        notificationRepository.save(notification);
        return ResponseEntity.ok("Disabe User with ID " + userId + " Seccess ");
    }

    public ResponseEntity<?> enableUser(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> {
                    throw new NotFoundException("Not Found User with ID : " + userId);
                }
        );
        user.setIsEnable(true);
        userRepository.save(user);
        User accOfAdmin = iCurrent.getUser();
        Notification notification = Notification.builder()
                .username(accOfAdmin.getName())
                .title("Remove Blogger")
                .avatar(accOfAdmin.getAvatar())
                .content(accOfAdmin.getName() + " Enable User with ID " + user.getId())
                .notificationStatus(NotificationStatus.ACCOUNT)
                .typeOf(1)
                .build();
        notificationRepository.save(notification);
        return ResponseEntity.ok("Enable User with ID " + userId + " Seccess ");
    }


    // service for Category
    public ResponseEntity<?> createCategory(UpdateCategoryRequest request) {
        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .thumbail(request.getThumbail())
                .build();
        categoryRepository.save(category);
        User accOfAdmin = iCurrent.getUser();
        Notification notification = Notification.builder()
                .username(accOfAdmin.getName())
                .title("Create Category New")
                .avatar(accOfAdmin.getAvatar())
                .content(accOfAdmin.getName() + " Create Category with ID " + category.getId())
                .notificationStatus(NotificationStatus.UPDATE)
                .typeOf(1)
                .build();
        notificationRepository.save(notification);
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
        User accOfAdmin = iCurrent.getUser();
        Notification notification = Notification.builder()
                .username(accOfAdmin.getName())
                .title("Update Category ")
                .content(accOfAdmin.getName() + " Update Category with ID " + category.getId())
                .notificationStatus(NotificationStatus.UPDATE)
                .typeOf(1)
                .build();
        notificationRepository.save(notification);
        return ResponseEntity.ok("Update Category Seccess !! ");
    }

    public ResponseEntity<?> deleteCategory(Integer categoryId) {
        Category acc = categoryRepository.findById(100).get();
        Category item = categoryRepository.findById(categoryId).orElseThrow(
                () -> {
                    throw new NotFoundException("Not found Category with ID" + categoryId);
                }
        );
        Integer itemId = item.getId();
        List<Product> products = productRepository.findByCategory_Id(categoryId);
        for (Product product : products) {
            product.setCategory(acc);
            productRepository.save(product);
        }
        categoryRepository.delete(item);
        User accOfAdmin = iCurrent.getUser();
        Notification notification = Notification.builder()
                .username(accOfAdmin.getName())
                .title("Delete Category ")
                .content(accOfAdmin.getName() + " Delete Category with ID " + itemId)
                .notificationStatus(NotificationStatus.UPDATE)
                .typeOf(1)
                .build();
        notificationRepository.save(notification);
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
        User accOfAdmin = iCurrent.getUser();
        Notification notification = Notification.builder()
                .username(accOfAdmin.getName())
                .title("Create Brand New")
                .avatar(accOfAdmin.getAvatar())
                .content(accOfAdmin.getName() + " Create Brand with ID " + brand.getId())
                .notificationStatus(NotificationStatus.UPDATE)
                .typeOf(1)
                .build();
        notificationRepository.save(notification);
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
        User accOfAdmin = iCurrent.getUser();
        Notification notification = Notification.builder()
                .username(accOfAdmin.getName())
                .title("Update Brand New")
                .avatar(accOfAdmin.getAvatar())
                .content(accOfAdmin.getName() + " Update Brand with ID " + item.getId())
                .notificationStatus(NotificationStatus.UPDATE)
                .typeOf(1)
                .build();
        notificationRepository.save(notification);
        return ResponseEntity.ok("Update Brand Seccess !!");
    }

    public ResponseEntity<?> deleteBrand(Integer brandId) {
        Brand acc = brandRepository.findById(100).get();
        Brand item = brandRepository.findById(brandId).orElseThrow(
                () -> {
                    throw new NotFoundException("Not found Brand with ID" + brandId);
                }
        );
        Integer idOfitem = item.getId();
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
        User accOfAdmin = iCurrent.getUser();
        Notification notification = Notification.builder()
                .username(accOfAdmin.getName())
                .title("Delete Brand New")
                .avatar(accOfAdmin.getAvatar())
                .content(accOfAdmin.getName() + " Delete Brand with ID " + idOfitem)
                .notificationStatus(NotificationStatus.UPDATE)
                .typeOf(1)
                .build();
        notificationRepository.save(notification);
        return ResponseEntity.ok("Delete Brand Seccess !! ");
    }

    public ManagentUser getUserById(Integer userId) {
        Optional<ManagentUser> user = userRepository.findUser(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Not found User with Id" + userId);
        }
        return user.get();
    }

    // service for Notication
    public Page<NotificationInfo> getNotification(Integer page, Integer pageSize, Integer choose) {
        if (choose == 2) {
            return notificationRepository.findNoticationInfoAll(PageRequest.of(page, pageSize));
        }
        if (choose == 1) {
            return notificationRepository.findByNouticationOfAdmin(PageRequest.of(page, pageSize));
        }
        return notificationRepository.findByNouticationOfUser(PageRequest.of(page, pageSize));
    }

    public List<NotificationInfo> getNotificationOfUser() {
        List<NotificationInfo> notificationInfoList = notificationRepository.findByCreateAtAndTypeOfOrderByCreateAtDesc(LocalDate.now().minusDays(1), 0);
        if (notificationInfoList.size() > 5) {
            return notificationInfoList.subList(0, 4);
        }
        return notificationInfoList;
    }

    public List<NotificationInfo> getNotificationOfAdmin() {
        List<NotificationInfo> notificationInfoList = notificationRepository.findByCreateAtAndTypeOfOrderByCreateAtDesc(LocalDate.now().minusDays(1), 1);
        if (notificationInfoList.size() > 5) {
            return notificationInfoList.subList(0, 4);
        }
        return notificationInfoList;
    }

    public ResponseEntity<?> deleteNotification(Integer notificationId) {
        Notification item = notificationRepository.findById(notificationId).orElseThrow(
                () -> {
                    throw new NotFoundException("Not found Notication ID with + " + notificationId);
                }
        );
        notificationRepository.delete(item);
        return ResponseEntity.ok("Delete Notification Seccess !! ");
    }

    // Service for Order..

    public Page<PaymentInfo> getOrderAll(Integer page, Integer pageSize, Integer choose, Integer time) {
        if (choose == 1) {
            if (time == 1) {
                return paymentRepository.findPayments(PageRequest.of(page, pageSize));
            }
            if (time == 2) {
                return paymentRepository.findByCreateAtGreaterThanOrderByIdAsc(LocalDate.now().minusDays(7), PageRequest.of(page, pageSize));
            }
            if (time == 3) {
                return paymentRepository.findByCreateAtGreaterThanOrderByIdAsc(LocalDate.now().minusDays(31), PageRequest.of(page, pageSize));
            }
        }
        if (choose == 2) {
            if (time == 1) {
                return paymentRepository.findPayments(PaymentStatus.SUCCESS, PageRequest.of(page, pageSize));
            }
            if (time == 2) {
                return paymentRepository.findByPaymentStatusOrderByIdDesc(PageRequest.of(page, pageSize), PaymentStatus.SUCCESS, LocalDate.now().minusWeeks(1));
            }
            if (time == 3) {
                return paymentRepository.findByPaymentStatusOrderByIdDesc(PageRequest.of(page, pageSize), PaymentStatus.SUCCESS, LocalDate.now().minusMonths(1));
            }
        }
        if (choose == 3) {
            if (time == 1) {
                return paymentRepository.findPayments(PaymentStatus.INITIAL, PageRequest.of(page, pageSize));
            }
            if (time == 2) {
                return paymentRepository.findByPaymentStatusOrderByIdDesc(PageRequest.of(page, pageSize), PaymentStatus.INITIAL, LocalDate.now().minusWeeks(1));
            }
            if (time == 3) {
                return paymentRepository.findByPaymentStatusOrderByIdDesc(PageRequest.of(page, pageSize), PaymentStatus.INITIAL, LocalDate.now().minusMonths(1));
            }
        }
        if (choose == 4) {
            if (time == 1) {
                return paymentRepository.findPayments(PaymentStatus.PROCEED, PageRequest.of(page, pageSize));
            }
            if (time == 2) {
                return paymentRepository.findByPaymentStatusOrderByIdDesc(PageRequest.of(page, pageSize), PaymentStatus.PROCEED, LocalDate.now().minusWeeks(1));
            }
            if (time == 3) {
                return paymentRepository.findByPaymentStatusOrderByIdDesc(PageRequest.of(page, pageSize), PaymentStatus.PROCEED, LocalDate.now().minusMonths(1));
            }
        }
        if (choose == 5) {
            if (time == 1) {
                return paymentRepository.findPayments(PaymentStatus.CANCLE, PageRequest.of(page, pageSize));
            }
            if (time == 2) {
                return paymentRepository.findByPaymentStatusOrderByIdDesc(PageRequest.of(page, pageSize), PaymentStatus.CANCLE, LocalDate.now().minusDays(7));
            }
            if (time == 3) {
                return paymentRepository.findByPaymentStatusOrderByIdDesc(PageRequest.of(page, pageSize), PaymentStatus.CANCLE, LocalDate.now().minusDays(31));
            }
        }
        return paymentRepository.findPayments(PageRequest.of(page, pageSize));
    }

    public PaymentInfo getPaymentById(Integer paymentId) {
        return paymentRepository.findById_Payment(paymentId);
    }


    // service for ChartJ
    public ChartjResponse getTarget(Integer choose){ //get Fintech
        ChartjResponse response = new ChartjResponse();
        List<LocalDate> time = new ArrayList<>();
        List<Double> totalPrice = new ArrayList<>();
        if(choose == 1){
            for (int i = 0; i <5 ; i++){
                time.add(LocalDate.now().minusDays(i+1));
            }
            for (int i = 0; i <5 ; i++){
                totalPrice.add(0.0);
            }
            List<ChartJ> chartJList = paymentRepository.getTarget(LocalDate.now().minusDays(7));
            for (int i = 0; i <chartJList.size() ; i++) {
                for (int j = 0; j < time.size();j++){
                    if(chartJList.get(i).getReceived() == time.get(j)){
                        totalPrice.add(j,chartJList.get(i).getTotalPrice());
                    }

                }

            }

        }


        for (int i = 0; i <5 ; i++){
            time.add(LocalDate.now().minusDays(i+1));
        }

        response.setTime(time);
        response.setTotalPrice(totalPrice);
        return response;
    }
}
