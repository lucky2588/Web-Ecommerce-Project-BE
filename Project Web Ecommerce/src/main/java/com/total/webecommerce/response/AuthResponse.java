package com.total.webecommerce.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.total.webecommerce.entity.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthResponse {
    @JsonProperty("auth")
    private UserDTO userDTO;
    private String token;
    @JsonProperty("isAuthenticated")
    private boolean isAuthenticated;
}
