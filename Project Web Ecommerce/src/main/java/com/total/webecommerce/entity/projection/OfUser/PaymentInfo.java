package com.total.webecommerce.entity.projection.OfUser;

import com.total.webecommerce.entity.Payment;
import com.total.webecommerce.entity.projection.OfAdmin.AccountBankInfo;
import com.total.webecommerce.entity.support.PaymentStatus;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

/**
 * Projection for {@link Payment}
 */
public interface PaymentInfo {
    Integer getId();

    String getAddress();

    Integer getTransport();

    Integer getType();

    String getText();

    String getThumbail();

    Double getPrice();

    PaymentStatus getPaymentStatus();

    MyOrderInfo getOrderBill();

    AccountBankInfo getAccountBank();
    LocalDate getCreateAt();
    UserInfo getUser();
    @RequiredArgsConstructor
    class PaymentInfoImpl implements PaymentInfo{
        private final Payment payment;
        @Override
        public Integer getId() {
            return payment.getId();
        }

        @Override
        public String getAddress() {
            return payment.getAddress();
        }

        @Override
        public Integer getTransport() {
            return payment.getTransport();
        }

        @Override
        public Integer getType() {
            return payment.getType();
        }

        @Override
        public String getText() {
            return payment.getText();
        }

        @Override
        public String getThumbail() {
            return payment.getThumbail();
        }

        @Override
        public LocalDate getCreateAt() {
            return payment.getCreateAt();
        }
        @Override
        public Double getPrice() {
            return payment.getPrice();
        }
        @Override
        public PaymentStatus getPaymentStatus() {
            return payment.getPaymentStatus();
        }
        @Override
        public MyOrderInfo getOrderBill() {
            return MyOrderInfo.of(payment.getOrderBill());
        }
        @Override
        public AccountBankInfo getAccountBank() {
            return AccountBankInfo.of(payment.getAccountBank());
        }

        @Override
        public UserInfo getUser() {
            return UserInfo.of(payment.getUser());
        }
    }

}