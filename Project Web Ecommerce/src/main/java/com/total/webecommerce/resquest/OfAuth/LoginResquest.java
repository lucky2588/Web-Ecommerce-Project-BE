package com.total.webecommerce.resquest.OfAuth;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResquest {
    @NotEmpty(message = "Không được để trống !!")
    private String email;
    @NotEmpty(message = "Không được để trống")
    private String password;
}
