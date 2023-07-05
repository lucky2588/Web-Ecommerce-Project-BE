package com.total.webecommerce.controller;

import com.total.webecommerce.entity.projection.OfUser.OrderBillInfo;
import com.total.webecommerce.entity.projection.OfUser.PaymentInfo;
import com.total.webecommerce.entity.projection.OfUser.ProductInfo;
import com.total.webecommerce.resquest.OfOrder.AddProductToOrder;
import com.total.webecommerce.resquest.OfOrder.ChangeProductFromOrder;
import com.total.webecommerce.resquest.OfOrder.RemoveOrderItem;
import com.total.webecommerce.resquest.OfUser.CreatePaymentResquest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.total.webecommerce.service.OrderSerivce;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/user")
public class OrderController {
    @Autowired
    private OrderSerivce serivce;
    @GetMapping("myOrder/{email}")
    public OrderBillInfo getMyOrder(@PathVariable String email) {
        return serivce.getMyOrder(email);
    }

    @GetMapping("ProductSimilarInOrder/{email}")
    public List<ProductInfo> getProductForOrder(@PathVariable String email) {
        return serivce.getProductInfo(email);
    }
    @PostMapping("addProductToOrder")
    public ResponseEntity<?> addProduct(@Valid @RequestBody AddProductToOrder resquest) {
        return serivce.addProductToOrder(resquest);
    }
    @PostMapping("removeProductFromOrder")
    public ResponseEntity<?> removeProduct(@RequestBody RemoveOrderItem resquest) {
        return serivce.removeProductFromOrder(resquest);
    }
    @PostMapping("changleProductFormOrder")
    public ResponseEntity<?> changleProduct(@Valid @RequestBody ChangeProductFromOrder resquest) {
        return serivce.changeProductFormOrder(resquest);
    }
    @GetMapping("getOrder/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable Integer orderId){
        return serivce.getOrderById(orderId);
    }
    @PostMapping("CreatePayment")
    public ResponseEntity<?> createPayment(@Valid @RequestBody CreatePaymentResquest resquest){
        return serivce.createPayment(resquest);
    }
    @GetMapping("getMyBill/{email}")
    public List<PaymentInfo> getMyBill(@PathVariable String email){
        return serivce.getMyBill(email);
    }
    @GetMapping("getPayment/{paymentId}")
    public PaymentInfo getMyBill(@PathVariable Integer paymentId){
        return serivce.getPaymentById(paymentId);
    }

}
