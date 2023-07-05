package com.total.webecommerce.entity.projection.Public;

import com.total.webecommerce.entity.Blog;
import com.total.webecommerce.entity.projection.OfUser.UserInfo;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/**
 * Projection for {@link Blog}
 */
public interface BlogInfo {
    Integer getId();

    String getTitle();

    String getContent();

    String getThumbail();

    String getDescription();

    LocalDateTime getCreateAt();

    BrandInfo getBrand();

    Integer getViewBlog();
    Integer getStatusBlog();

    UserInfo getUser();
    @RequiredArgsConstructor
    class BlogInfoImpl implements BlogInfo{
        private final Blog blog;
        @Override
        public Integer getId() {
            return blog.getId();
        }
        @Override
        public String getTitle() {
            return blog.getTitle();
        }
        @Override
        public String getContent() {
            return blog.getContent();
        }
        @Override
        public String getThumbail() {
            return blog.getThumbail();
        }
        @Override
        public String getDescription() {
            return blog.getDescription();
        }

        @Override
        public Integer getStatusBlog() {
            return blog.getStatusBlog();
        }

        @Override
       public Integer getViewBlog(){
            return blog.getViewBlog();
        }

        @Override
        public LocalDateTime getCreateAt() {
            return blog.getCreateAt();
        }
        @Override
        public BrandInfo getBrand() {
            return BrandInfo.of(this.blog.getBrand());
        }
        @Override
        public UserInfo getUser() {
            return UserInfo.of(this.blog.getUser());
        }
    }

    static BlogInfo of(Blog blog) {
        return new BlogInfoImpl(blog);
    }



}