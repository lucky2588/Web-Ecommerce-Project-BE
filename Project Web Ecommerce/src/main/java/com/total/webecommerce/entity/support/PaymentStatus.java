package com.total.webecommerce.entity.support;

public enum PaymentStatus {
    NOT_Accepted("Xóa Đơn Hàng !! "),

    INITIAL("Trạng thái khởi tạo"),
    Not_Receive("Trạng thái không nhận hàng , trả hàng"),
    CANCLE("Trạng thái hủy đơn hàng"),
    REFUND("Trạng thái hoàn đơn hàng "),
    SUCCESS("Trạng thái thành công"),
    PROCEED("Trạng thái đang diễn ra");

    private String message;
    private PaymentStatus (String value){
        this.message = value;
    }

    public String getMessage(){
        return message;
    }
}
