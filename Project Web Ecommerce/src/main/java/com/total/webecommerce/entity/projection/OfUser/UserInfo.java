package com.total.webecommerce.entity.projection.OfUser;

import com.total.webecommerce.entity.User;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/**
 * Projection for {@link User}
 */
public interface UserInfo {
    Integer getId();

    String getName();

    String getEmail();

    String getAddress();

    String getPhone();

    LocalDateTime getCreateAt();

    String getAvatar();

    @RequiredArgsConstructor
    class UserInfoImpl implements UserInfo {
        private final User user;

        @Override
        public Integer getId() {
            return user.getId();
        }

        @Override
        public String getName() {
            return user.getName();
        }

        @Override
        public String getEmail() {
            return user.getEmail();
        }

        @Override
        public String getAddress() {
            return user.getAddress();
        }

        @Override
        public String getPhone() {
            return user.getPhone();
        }

        @Override
        public String getAvatar() {
            return user.getAvatar();
        }
        @Override
        public LocalDateTime getCreateAt() {
            return user.getCreateAt();
        }
    }

    static UserInfo of(User user) {
        return new UserInfoImpl(user);
    }

}