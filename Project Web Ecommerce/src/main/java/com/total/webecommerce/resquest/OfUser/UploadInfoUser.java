package com.total.webecommerce.resquest.OfUser;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UploadInfoUser {
    @NotBlank(message = "Tên không được để trống")
    private String name;
    @NotBlank(message = "địa chỉ  không được để trống")
    private String address;
    @Pattern(regexp = "^0\\d{8,10}$", message = "Số điện thoại không hợp lệ")
    @Size(min = 9, max = 11, message = "Số điện thoại phải có độ dài từ 9 đến 11 ký tự")
    private String phoneNumber;


}
