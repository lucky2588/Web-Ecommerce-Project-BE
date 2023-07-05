package com.total.webecommerce.resquest.OfUser;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ChangePassword {
    @NotNull(message = "Không được để trống")
    private String email;
    @NotNull(message = "Không được để trống !! ")
    @Size( message = "Độ dài phải đủ 6 ký tự ")
    private String passwordOld;
    @NotNull(message = "Không được để trống !! ")
    @Size(min = 6 , message = "Độ dài phải đủ 6 ký tự ")
    private String passwordNew;
}
