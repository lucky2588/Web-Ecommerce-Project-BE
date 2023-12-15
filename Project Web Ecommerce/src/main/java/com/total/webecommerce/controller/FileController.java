package com.total.webecommerce.controller;

import com.total.webecommerce.entity.Image;
import com.total.webecommerce.entity.ImageBlog;
import com.total.webecommerce.entity.ImageProduct;
import com.total.webecommerce.response.FileResponse;
import com.total.webecommerce.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/files")
@Slf4j
public class FileController {
    private final FileService fileService;
    // 1. Upload file ảnh của User
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping("/{userId}")
    public ResponseEntity<?> uploadFile(@PathVariable Integer userId,@ModelAttribute("file") MultipartFile file) {
        FileResponse fileResponse = fileService.uploadFile(userId,file);
        return new ResponseEntity<>(fileResponse, HttpStatus.CREATED);
    }
    // 2. Xem thông tin file của User
    @GetMapping(value = "{id}")
    public ResponseEntity<?> readFile(@PathVariable Integer id) {
        Image image = fileService.readFile(id);
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(image.getType()))
                .body(image.getData());
    }
       // đọc  file ảnh của User
    @GetMapping("getImage/{userId}/{imageId}")
    public ResponseEntity<?> takeImageUser(@PathVariable int userId,@PathVariable int imageId){
        Image images = fileService.readImageUser(userId,imageId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(images.getType()))
                .body(images.getData());
    }

    // 2. Upload Ảnh của Book
    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    @PostMapping("blog/{blogId}")
    public ResponseEntity<?> uploadFileBlog(@ModelAttribute("file") MultipartFile file,@PathVariable Integer blogId) {
        FileResponse fileResponse = fileService.uploadImageBook(file,blogId);
        return new ResponseEntity<>(fileResponse, HttpStatus.CREATED);
    }
    @GetMapping("blog/{blogId}/{imageBlogId}")
    public ResponseEntity<?> getImageBlog(@PathVariable int blogId,@PathVariable int imageBlogId){
        ImageBlog images = fileService.readImageBlog(blogId,imageBlogId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(images.getType()))
                .body(images.getData());
    }



    // 2. Upload Ảnh của Product
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("product/{productId}")
    public ResponseEntity<?> uploadFileProduct(@ModelAttribute("file") MultipartFile file,@PathVariable Integer productId) {
        FileResponse fileResponse = fileService.uploadImageProduct(file,productId);
        return new ResponseEntity<>(fileResponse, HttpStatus.CREATED);
    }

@GetMapping("product/{productId}/{productImageId}")
public ResponseEntity<?> getImageProduct(@PathVariable int productId,@PathVariable int productImageId){
    ImageProduct images = fileService.readProductImage(productId,productImageId);
    return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(images.getType()))
            .body(images.getData());
}


}
