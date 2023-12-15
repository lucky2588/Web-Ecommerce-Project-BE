package com.total.webecommerce.service;

import com.total.webecommerce.entity.Favorites;
import com.total.webecommerce.entity.Product;
import com.total.webecommerce.entity.User;
import com.total.webecommerce.entity.projection.OfUser.FavoritesInfo;
import com.total.webecommerce.entity.projection.OfUser.UserInfo;
import com.total.webecommerce.exception.BadResquestException;
import com.total.webecommerce.exception.NotFoundException;
import com.total.webecommerce.response.WishListResponse;
import com.total.webecommerce.respository.OfUser.FavoritesRepository;
import com.total.webecommerce.respository.OfProduct.ProductRepository;
import com.total.webecommerce.respository.OfUser.UserRepository;
import com.total.webecommerce.resquest.OfUser.AddWishList;
import com.total.webecommerce.resquest.OfUser.ChangePassword;
import com.total.webecommerce.resquest.OfUser.UploadInfoUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

import java.util.Optional;
import java.util.Set;

@Service
@Slf4j

public class UserService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FavoritesRepository favorites;

    @Autowired
    private PasswordEncoder encoder = new BCryptPasswordEncoder();

    // tim User theo Email
    public UserInfo getUser(String email){
        return userRepository.findEmailWithInfo(email).orElseThrow(
                ()-> {
                    throw new NotFoundException("Không tìm thấy User này !!! ");
                }
        );
    }
    public ResponseEntity<?> changePassword(ChangePassword resquest) {

        String passNew = encoder.encode(resquest.getPasswordNew());
        String passOld = encoder.encode(resquest.getPasswordOld());
        if(passNew.equals(resquest.getPasswordOld())){
            throw  new BadResquestException("Mật khẩu trùng với mật khẩu cũ");
        }
        Optional<User> user = userRepository.findByEmail(resquest.getEmail());
        if(user.isEmpty()){
            throw new NotFoundException("Không tìm thấy Email này");
        }
        if(!encoder.matches(resquest.getPasswordOld(), user.get().getPassword()) ){
            throw new BadResquestException("Mật khẩu cũ không đúng ... ");
        }
        user.get().setPassword(encoder.encode(resquest.getPasswordNew()));
        userRepository.save(user.get());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Đổi mật khẩu thành công !! ");
    }
    // cập nhật thông tin User
    public ResponseEntity<?> uploadUser(Integer userId,UploadInfoUser resquest) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()){
            throw new NotFoundException("Không tìm thấy User này");
        }
        user.get().setName(resquest.getName());
        user.get().setAddress(resquest.getAddress());
        user.get().setPhone(resquest.getPhoneNumber());
        userRepository.save(user.get());
    return ResponseEntity.status(HttpStatus.ACCEPTED).body("Cập nhật thông tin thành công !! ");
    }
    public WishListResponse findFavorites(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        FavoritesInfo favoritesInfos = favorites.findByUser_IdAndStatusTrue(user.get().getId());
        if(favoritesInfos == null){
           WishListResponse wishListResponse = WishListResponse.builder()
                   .check(0)
                   .favoritesInfo(null)
                   .build();
           return wishListResponse;
        }
         WishListResponse wishListResponse = WishListResponse.builder()
                .check(1)
                .favoritesInfo(favoritesInfos)
                .build();
        return wishListResponse;
    }
    // update WishList cho User
    public ResponseEntity<?> addWishList(AddWishList resquest){
        Optional<User> user = userRepository.findByEmail(resquest.getEmail());
        Product product = productRepository.findById(resquest.getProductId()).orElse(null);
        Favorites item = favorites.findByUser_Email(resquest.getEmail());
        if(item == null){
            item.setStatus(true);
            Set<Product> products = new HashSet<>();
            products.add(product);
         Favorites newFavorites = Favorites.builder()
                 .products(products)
                 .user(user.get())
                 .status(true)
                 .build();
         favorites.save(newFavorites);
         return ResponseEntity.ok("Thêm vào danh sách Yêu thích thành công ");
        }

        if(item.getProducts().contains(product)){
            throw new BadResquestException("Sản phẩm này đã có trong danh sách yêu thích");
        }
        item.setStatus(true);
        item.getProducts().add(product);
        favorites.save(item);
        return ResponseEntity.ok("Đã thêm SP vào danh sách yêu thích");
    }

    public ResponseEntity<?> removeWishList(AddWishList resquest){
        Optional<User> user = userRepository.findByEmail(resquest.getEmail());
        Product product = productRepository.findById(resquest.getProductId()).orElse(null);
        Favorites item = favorites.findByUser_Email(resquest.getEmail());
        item.getProducts().remove(product);
        if(item.getProducts().isEmpty()){
            item.setStatus(false);
        }
        favorites.save(item);
        return ResponseEntity.ok("Xóa sản phẩm khỏi danh sách yêu thích thành công !! ");
    }








}
