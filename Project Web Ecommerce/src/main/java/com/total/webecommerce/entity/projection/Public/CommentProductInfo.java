package com.total.webecommerce.entity.projection.Public;

import com.total.webecommerce.entity.CommentProduct;
import com.total.webecommerce.entity.projection.OfUser.UserInfo;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/**
 * Projection for {@link CommentProduct}
 */
public interface CommentProductInfo {
    Integer getId();

    String getContent();

    LocalDateTime getCreateAt();
    UserInfo getUser();
    @RequiredArgsConstructor
    class CommentProductInfoImpl implements CommentProductInfo{
        private final CommentProduct commentProduct;

        @Override
        public Integer getId() {
            return commentProduct.getId();
        }

        @Override
        public String getContent() {
            return commentProduct.getContent();
        }

        @Override
        public LocalDateTime getCreateAt() {
            return commentProduct.getCreateAt();
        }

        @Override
        public UserInfo getUser() {
            return UserInfo.of(commentProduct.getUser());
        }
    }
    static CommentProductInfo of(CommentProduct commentProduct){
        return new CommentProductInfoImpl(commentProduct);
    }
}