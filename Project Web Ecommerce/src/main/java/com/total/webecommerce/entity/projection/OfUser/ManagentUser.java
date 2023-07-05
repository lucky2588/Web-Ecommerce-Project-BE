package com.total.webecommerce.entity.projection.OfUser;



import com.total.webecommerce.entity.Role;
import com.total.webecommerce.entity.User;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Projection for {@link User}
 */
public interface ManagentUser {
    Integer getId();

    String getName();

    String getEmail();

    String getAddress();

    String getPhone();

    String getAvatar();
    LocalDateTime getCreateAt();

    List<RoleInfo> getRoles();
    Boolean getIsEnable();

    @RequiredArgsConstructor
    class UsersInfo implements ManagentUser {
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
        public Boolean getIsEnable() {
            return user.getIsEnable();
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
        @Override
        public List<RoleInfo> getRoles() {
            return user.getRoles().stream().map(e->RoleInfo.of(e)).collect(Collectors.toList());
        }
    }

    static ManagentUser of(User user) {
        return new UsersInfo (user);
    }

}