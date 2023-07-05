package com.total.webecommerce.entity.projection.Public;

import com.total.webecommerce.entity.CommentOfBlog;
import com.total.webecommerce.entity.projection.OfUser.UserInfo;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/**
 * Projection for {@link CommentOfBlog}
 */
public interface CommentBlog {
    Integer getId();

    String getContent();

    LocalDateTime getCreateAt();
    UserInfo getUser();
    @RequiredArgsConstructor
    class CommentBlogImpl implements CommentBlog{
        private final CommentOfBlog comment;
        @Override
        public Integer getId() {
            return comment.getId();
        }

        @Override
        public String getContent() {
            return comment.getContent();
        }

        @Override
        public LocalDateTime getCreateAt() {
            return comment.getCreateAt();
        }
        @Override
        public UserInfo getUser() {
            return UserInfo.of(comment.getUser());
        }
    }
    static CommentBlog of(CommentOfBlog comment){
        return new CommentBlogImpl(comment);
    }
}