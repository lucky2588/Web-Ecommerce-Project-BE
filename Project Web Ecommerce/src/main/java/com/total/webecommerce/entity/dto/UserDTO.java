package com.total.webecommerce.entity.dto;

import com.total.webecommerce.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Integer id;
    private String name;
    private String email;
    private String avatar;
    private List<Role> roles;
}
