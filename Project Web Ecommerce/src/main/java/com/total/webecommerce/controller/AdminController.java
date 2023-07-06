package com.total.webecommerce.controller;

import com.total.webecommerce.entity.dto.BestBuyer;
import com.total.webecommerce.entity.projection.NotificationInfo;
import com.total.webecommerce.entity.projection.OfUser.ManagentUser;
import com.total.webecommerce.entity.projection.OfUser.PaymentInfo;
import com.total.webecommerce.entity.projection.OfUser.UserInfo;
import com.total.webecommerce.response.OverviewInfo;
import com.total.webecommerce.resquest.OfOther.UpdateBrandRequest;
import com.total.webecommerce.resquest.OfOther.UpdateCategoryRequest;
import com.total.webecommerce.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("api/v1/admin")
public class AdminController {
    @Autowired
    private AdminService service;
    // to do about get Infomation Total .... Dashoash
    @GetMapping("getInfoTotal") // get Info Total
    public OverviewInfo getInfo(){
        return service.getInfoGarenal();
    }
    @GetMapping("getBuyer")
    public List<BestBuyer> getBuyer(){
        return service.getBestBuyer();
    }
    // service for User
    @GetMapping("getUser") // get Page with User
    public Page<UserInfo> getUser(@RequestParam(defaultValue = "0") Integer page , @RequestParam(defaultValue = "5") Integer pageSize){
        return service.getPageWithUser(page,pageSize);
    }
    @GetMapping("getUserByEmail")
    public UserInfo getUser(@RequestParam String email) {
        return service.getUser(email);
    }
    @GetMapping("getUser/{userId}") // get Page with User
    public ManagentUser getUser(@PathVariable Integer userId){
        return service.getUserById(userId);
    }

    @PostMapping("uptoBlogger/{userId}")
    public ResponseEntity<?> uptoBlogger(@PathVariable Integer userId){
        return service.uptoBlogerr(userId);
    }
    @PostMapping("removeBlogger/{userId}")
    public ResponseEntity<?> removeBlogger(@PathVariable Integer userId){
        return service.removeBlogger(userId);
    }
    @PostMapping("disableUser/{userId}")
    public ResponseEntity<?> disableUser(@PathVariable Integer userId){
        return service.disableUser(userId);
    }
    @PostMapping("enableUser/{userId}")
    public ResponseEntity<?> enableUser(@PathVariable Integer userId){
        return service.enableUser(userId);
    }

// service for Other
    //for Category ...
    @PostMapping("createCategory")
    public ResponseEntity<?> createCategory(@Valid @RequestBody UpdateCategoryRequest request){
        return service.createCategory(request);
    }
    @PostMapping("updateCategory")
    public ResponseEntity<?> updateCategory(@Valid @RequestBody UpdateCategoryRequest request){
        return service.updateCategory(request);
    }

    @DeleteMapping("deleteCategory/{categoryId}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable Integer categoryId){
        return service.deleteCategory(categoryId);
    }
    // for Brand
    @PostMapping("createBrand")
    public ResponseEntity<?> createBrand(@Valid @RequestBody UpdateBrandRequest request){
        return service.createBrand(request);
    }
    @PostMapping("updateBrand")
    public ResponseEntity<?> updateBrand(@Valid @RequestBody UpdateBrandRequest request){
        return service.updateBrand(request);
    }
    @DeleteMapping("deleteBrand/{brandId}")
    public ResponseEntity<?> deleteBrandById(@PathVariable Integer brandId){
        return service.deleteBrand(brandId);
    }

// service for Notification
    @GetMapping("getNotificationInfo/{choose}")
    public Page<NotificationInfo> getNotification(@PathVariable Integer choose,@RequestParam(defaultValue = "0") Integer page , @RequestParam(defaultValue = "10") Integer pageSize){
        return service.getNotification(page,pageSize,choose);
    }

    @GetMapping("getNotificationOfUser")
    public List<NotificationInfo> getNotificationOfUser(){
        return service.getNotificationOfUser();
    }

    @GetMapping("getNotificationOfAdmin")
    public List<NotificationInfo> getNotificationOfAdmin(){
        return service.getNotificationOfAdmin();
    }

    @DeleteMapping("deleteNotification/{notificationID}")
    public ResponseEntity<?> deleteNotification(@PathVariable Integer notificationID){
        return service.deleteNotification(notificationID);
    }


    // service for product ....
//    @GetMapping("ProductTop")
//    public List<OrderItemInfo> getProductTop(){
//
//    }









     // service for order bill , payment ....

    // get Order Today
    @GetMapping("getOrderToday")
    public List<PaymentInfo> getPayments(){
        return service.getPayments();
    }



}
