package com.total.webecommerce.controller;

import com.total.webecommerce.entity.projection.OfUser.UserInfo;
import com.total.webecommerce.response.WishListResponse;
import com.total.webecommerce.respository.OfUser.FavoritesRepository;
import com.total.webecommerce.respository.OfProduct.ProductRepository;
import com.total.webecommerce.resquest.OfUser.AddWishList;
import com.total.webecommerce.resquest.OfUser.ChangePassword;
import com.total.webecommerce.resquest.OfUser.UploadInfoUser;
import com.total.webecommerce.service.OrderSerivce;
import com.total.webecommerce.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private OrderSerivce orderSerivce;
    @Autowired
    private FavoritesRepository favorites;
    @Autowired
    private ProductRepository productRepository;
    @GetMapping("getUser")
    public UserInfo getUser(@RequestParam String email) {
        return userService.getUser(email);
    }

    @PostMapping("changePassword")
    public ResponseEntity<?> changlePassword(@Valid @RequestBody ChangePassword resquest) {
        return userService.changePassword(resquest);

    }
    @PostMapping("updateUser/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Integer userId, @Valid @RequestBody UploadInfoUser resquest) {
        return userService.uploadUser(userId, resquest);

    }

    @GetMapping("findFavorites")
    public WishListResponse getFavorites(@RequestParam String email) {
        return userService.findFavorites(email);
    }

    @PostMapping("addItemFavorites")
    public ResponseEntity<?> addFavorites(@RequestBody AddWishList resquest) {
        return userService.addWishList(resquest);
    }

    @PostMapping("removeItemFavorites")
    public ResponseEntity<?> removeItem(@RequestBody AddWishList resquest) {
        return userService.removeWishList(resquest);
    }


}
