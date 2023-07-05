package com.total.webecommerce.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
public class OverviewInfo {
    private Integer numsUser;
    private Integer numsBlog;
    private Integer numsPayment;
    private Integer numsProductBuy;
    private Double totalSales;
    private Integer orderStatus;
    private Integer orderProcess;
}
