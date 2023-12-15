package com.total.webecommerce.controller;
import com.total.webecommerce.entity.dto.BlogDTO;
import com.total.webecommerce.entity.projection.Public.BlogInfo;
import com.total.webecommerce.entity.projection.Public.CommentBlog;
import com.total.webecommerce.resquest.OfBlog.CommentBlogResquest;
import com.total.webecommerce.resquest.OfBlog.CreateBlogResquest;
import com.total.webecommerce.resquest.OfBlog.UpdateBlogResquest;
import com.total.webecommerce.service.BlogService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("api/v1/public")
public class BlogController {
    @Autowired
    private BlogService service;
    // Các API về BlogWeb bao gồm : Lấy ra danh sách list Blog , Blog chi tiết ....
    @GetMapping("getBlogs")
    public Page<BlogInfo> getBlogs(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "6") Integer pageSize){
        return service.getBlogs(page,pageSize);
    }
    // lấy danh sách Blog Chi tiết theo Id trả về
    @GetMapping("blog/{blogId}")
    public BlogInfo getBlogById(@PathVariable Integer blogId){
        return service.getBlogById(blogId);
    }
    // lấy danh sách top 4 Blog có view cao nhất
    @GetMapping("blog/topView")
    public List<BlogDTO> getBlogTopView(){
        return service.getBlogByView();
    }

    // lấy danh sách comment của Blog
    @GetMapping("commentBlog/{blogId}")
    public Page<CommentBlog> getCommentBlog(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "5") Integer pageSize,@PathVariable Integer blogId){
        return service.getBlogWithComment(page,pageSize,blogId);
    }
    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR','USER')")
    // Gửi comment cho Blog
    @PostMapping("sendCommentBlog/{blogId}")
    public ResponseEntity<?> sendCommentBlog(@PathVariable Integer blogId , @Valid @RequestBody CommentBlogResquest comment){
        return service.sendCommentOfBlog(blogId,comment);
    }

    // tìm 2 bài blog có nội dung tương tự
    @GetMapping("findBrand/{blogId}/{brandId}")
    public List<BlogDTO> findBrand(@PathVariable Integer blogId,@PathVariable Integer brandId){
        return service.findBrand(blogId,brandId);
    }
    // for Admin
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("getBlogsOfAdmin")
    public Page<BlogInfo> getBlogsOfAdmin(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "6") Integer pageSize){
        return service.getBlogsOfAdmin(page,pageSize);
    }
    // Delete Comment for Blog
    @PreAuthorize("hasAnyRole('ADMIN','USER','AUTHOR')")
    @DeleteMapping("deleteCommentOfBlog/{commentId}")
    public void deleteCommentOfBlog(@PathVariable Integer commentId){
         service.deleteCommentOfBlog(commentId);
    }
    @GetMapping("getBlogsOfAuthor")
    public Page<BlogInfo> getBlogsOfAuthor(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "6") Integer pageSize){
        return service.getBlogsOfAuthor(page,pageSize);
    }
    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    @PostMapping("updateBlog")
    public ResponseEntity<?> updateBlog(@Valid @RequestBody UpdateBlogResquest resquest){
        return service.updateBlog(resquest);
    }
    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    @PostMapping("createBlog")
    public Integer createBlog(@RequestBody CreateBlogResquest resquest){
        return service.createBlog(resquest);
    }
    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    @DeleteMapping("deleteBlog/{blogId}")
    public ResponseEntity<?> deleteBlog(@PathVariable Integer blogId){
       return service.deleteBlog(blogId);
    }
}
