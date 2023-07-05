package com.total.webecommerce.respository.OrBlog;

import com.total.webecommerce.entity.CommentOfBlog;
import com.total.webecommerce.entity.projection.Public.CommentBlog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentOfBlogRepository extends JpaRepository<CommentOfBlog, Integer> {
    List<CommentBlog> findByBlog_Id(Integer id);

}