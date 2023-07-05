package com.total.webecommerce.resquest.OfAuth;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResquest{
    @NotBlank(message = "Tên không được để trống")
    private String name;
    @NotBlank(message = "địa chỉ  không được để trống")
    private String address;
    @Email(message = "Email không hợp lệ")
    private String email;
    @Size(min = 6, max = 20, message = "Mật khẩu phải từ 6 đến 20 ký tự")
    private String password;
    @Pattern(regexp = "^0\\d{8,10}$", message = "Số điện thoại không hợp lệ")
    @Size(min = 9, max = 11, message = "Số điện thoại phải có độ dài từ 9 đến 11 ký tự")
    private String phoneNumber;
    @NotBlank(message = "Khong duoc de trong ")
    private String passwordRepart;
 }
