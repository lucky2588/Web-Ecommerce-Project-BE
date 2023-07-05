package com.total.webecommerce.entity.support;

public enum NotificationStatus {
    ACCOUNT("Account !! "),
    COMMENT("Active Comment"),
    BLOG("Blog"),
    PRODUCT("Product"),
    ORDERS("Order"),
    UPDATE("Update ");

    private String message;
    private NotificationStatus (String value){
        this.message = value;
    }

    public String getMessage(){
        return message;
    }
}
