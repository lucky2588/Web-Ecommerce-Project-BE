package com.total.webecommerce.respository.OrBlog;

import com.total.webecommerce.entity.ImageBlog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
public interface ImageBlogRepository extends JpaRepository<ImageBlog, Integer> {
    @Query("select i from ImageBlog i where i.blog.id = ?1")
    List<ImageBlog> findImageOfBlog(Integer id);
    List<ImageBlog> findByBlog_Id(Integer id);

    @Query("select i from ImageBlog i where i.id = ?1 and i.blog.id = ?2")
    ImageBlog findByIdAndBlog_Id(Integer id, Integer id1);
    
    




}