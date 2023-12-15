package com.total.webecommerce.respository.OrBlog;

import com.total.webecommerce.entity.CommentOfBlog;
import com.total.webecommerce.entity.projection.Public.CommentBlog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentOfBlogRepository extends JpaRepository<CommentOfBlog, Integer> {
    Page<CommentBlog> findByBlog_Id(Pageable pageable,Integer id);

    Page<CommentBlog> findByBlog_IdOrderByIdDesc(Integer id, Pageable pageable);



}