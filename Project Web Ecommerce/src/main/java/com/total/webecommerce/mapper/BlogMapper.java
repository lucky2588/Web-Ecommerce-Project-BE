package com.total.webecommerce.mapper;

import com.total.webecommerce.entity.dto.BlogDTO;
import com.total.webecommerce.entity.Blog;

public class BlogMapper {
    public static BlogDTO toBlogDto(Blog blog){
         BlogDTO blogDTO = BlogDTO.builder()
                 .id(blog.getId())
                 .title(blog.getTitle())
                 .description(blog.getDescription())
                 .thumbail(blog.getThumbail())
                 .viewBlog(blog.getViewBlog())
                 .brand_id(blog.getBrand().getId())
                 .user_id(blog.getUser().getId())
                 .content(blog.getContent())
                 .createAt(blog.getCreateAt())
                 .build();
         return blogDTO;
    }

}
