package com.total.webecommerce.entity.support;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChartJ {
    private LocalDate received;
    private Double totalPrice;
}
