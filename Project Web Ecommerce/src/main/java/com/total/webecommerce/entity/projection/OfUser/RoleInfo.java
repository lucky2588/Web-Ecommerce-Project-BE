package com.total.webecommerce.entity.projection.OfUser;

import com.total.webecommerce.entity.Role;
import lombok.RequiredArgsConstructor;

/**
 * Projection for {@link Role}
 */
public interface RoleInfo {
    Integer getId();

    String getName();
    @RequiredArgsConstructor
    class  RoleInfoImpl implements  RoleInfo{
        private final Role role;

        @Override
        public Integer getId() {
            return role.getId();
        }

        @Override
        public String getName() {
            return role.getName();
        }
    }
   static RoleInfo of(Role role){
        return new RoleInfoImpl(role);
    }
}