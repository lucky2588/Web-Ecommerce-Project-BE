package com.total.webecommerce.mapper;

import com.total.webecommerce.entity.dto.UserDTO;
import com.total.webecommerce.entity.User;

public class UserMapper {
    public static UserDTO toUserDto(User user) {
        UserDTO userDto = new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAvatar(),
                user.getRoles()
        );
        return userDto;
    }
}
