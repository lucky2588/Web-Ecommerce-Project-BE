package com.total.webecommerce.resquest.OfUser;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ForgetPassword {
    @NotNull(message = "Không được để trống")
    private String email;
    @NotNull(message = "Không được để trống !!!! ")
    private String token;
}
