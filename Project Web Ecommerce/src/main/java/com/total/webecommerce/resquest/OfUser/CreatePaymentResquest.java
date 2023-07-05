package com.total.webecommerce.resquest.OfUser;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreatePaymentResquest {
    private Integer userId;
    private Integer orderId;
    @NotBlank(message = " không được để trống")
    private String address;
    private String text;
    private Double totalPrice;
    private Integer transport;
    private Integer categoryPayment;
    private String nameAccount;
    private String numberAccount;
    private String brandBank;
}
