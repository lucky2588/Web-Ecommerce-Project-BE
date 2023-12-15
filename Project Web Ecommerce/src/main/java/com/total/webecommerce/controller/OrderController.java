package com.total.webecommerce.controller;

import com.total.webecommerce.entity.projection.OfUser.OrderBillInfo;
import com.total.webecommerce.entity.projection.OfUser.PaymentInfo;
import com.total.webecommerce.entity.projection.OfUser.ProductInfo;
import com.total.webecommerce.resquest.OfOrder.AddProductToOrder;
import com.total.webecommerce.resquest.OfOrder.ChangeProductFromOrder;
import com.total.webecommerce.resquest.OfOrder.ReasonCancle;
import com.total.webecommerce.resquest.OfOrder.RemoveOrderItem;
import com.total.webecommerce.resquest.OfUser.CreatePaymentResquest;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.total.webecommerce.service.OrderSerivce;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/order")
public class OrderController {
    @Autowired
    private OrderSerivce serivce;
    @GetMapping("myOrder/{userId}")
    public OrderBillInfo getMyOrder(@PathVariable Integer userId) {
        return serivce.getMyOrder(userId);
    }
    @GetMapping("ProductSimilarInOrder/{userId}")
    public List<ProductInfo> getProductForOrder(@PathVariable Integer userId) {
        return serivce.getProductInfo(userId);
    }
    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("addProductToOrder")
    public ResponseEntity<?> addProduct(@Valid @RequestBody AddProductToOrder resquest) {
        return serivce.addProductToOrder(resquest);
    }
    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("removeProductFromOrder")
    public ResponseEntity<?> removeProduct(@RequestBody RemoveOrderItem resquest) {
        return serivce.removeProductFromOrder(resquest);
    }
    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("changleProductFormOrder")
    public ResponseEntity<?> changleProduct(@Valid @RequestBody ChangeProductFromOrder resquest) {
        return serivce.changeProductFormOrder(resquest);
    }
    @GetMapping("getOrder/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable Integer orderId){
        return serivce.getOrderById(orderId);
    }
    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("CreatePayment")
    public ResponseEntity<?> createPayment(@Valid @RequestBody CreatePaymentResquest resquest){
        return serivce.createPayment(resquest);
    }
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("getMyBill/{userId}")
    public List<PaymentInfo> getMyBill(@PathVariable Integer userId){
        return serivce.getMyBill(userId);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("getPayments/{userId}")
    public Page<PaymentInfo> getPayments(@PathVariable Integer userId, @RequestParam(defaultValue = "0") Integer page , @RequestParam(defaultValue = "5") Integer pageSize){
        return serivce.getPayments(userId,page,pageSize);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("getPaymentsOfUser/{userId}")
    public List<PaymentInfo> getPayments(@PathVariable Integer userId){
        return serivce.getPaymentsForAdmin(userId);
    }

    // Hủy đơn hàng phía User
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @DeleteMapping("deleteOrder/{paymentId}")
    public ResponseEntity<?> deleteBillbyId(@PathVariable Integer paymentId){
       return serivce.deletePaymentByUser(paymentId);
    }
    @PreAuthorize("hasAnyRole('USER','ADMIN','AUTHOR')")
    @DeleteMapping("deleteAllOfOrderItem")
    public void deleteItem(){
        serivce.deleleItem();
    }


    // service for ADMIN
    @GetMapping("getOrders/{choose}/{time}")
    public Page<PaymentInfo> getOrdersAll(@PathVariable Integer choose, @PathVariable Integer time, @RequestParam(defaultValue = "0") Integer page , @RequestParam(defaultValue = "5") Integer pageSize){
        return serivce.getOrderAll(page,pageSize,choose,time);
    }
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("getPayment/{paymentId}")
    public PaymentInfo getMyPayment(@PathVariable Integer paymentId){
        return serivce.getPaymentById(paymentId);
    }
    // get Order Today
    @GetMapping("getOrderToday")
    public List<PaymentInfo> getPayments(){
        return serivce.getPayments();
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("getAccept/{paymentId}")
    public ResponseEntity<?> acceptPayment(@PathVariable Integer paymentId){
        return serivce.accpectPayment(paymentId);
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("returnOrder/{paymentId}")
    public ResponseEntity<?> cancleOrder(@PathVariable Integer paymentId){
        return serivce.returnOrder(paymentId);
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("cancleOrder/{paymentId}")
    public ResponseEntity<?> cancleOrder(@PathVariable Integer paymentId, @RequestBody ReasonCancle resquest){
        return serivce.cancleOrder(paymentId,resquest);
    }
    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("notReceiveOrder/{paymentId}")
    public ResponseEntity<?> notReceiveOrder(@PathVariable Integer paymentId, @RequestBody ReasonCancle resquest){
        return serivce.notReceiveOrder(paymentId,resquest);
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("refundOrder/{paymentId}")
    public ResponseEntity<?> refundOrder(@PathVariable Integer paymentId, @RequestBody ReasonCancle resquest){
        return serivce.refundOrder(paymentId,resquest);
    }
    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("receiveOrder/{paymentId}")
    public ResponseEntity<?> receiveOrder(@PathVariable Integer paymentId){
        return serivce.receiveOrder(paymentId);
    }




}
