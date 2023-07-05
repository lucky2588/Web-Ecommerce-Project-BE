package com.total.webecommerce.service;

import com.total.webecommerce.entity.*;
import com.total.webecommerce.entity.projection.OfUser.OrderBillInfo;
import com.total.webecommerce.entity.projection.OfUser.PaymentInfo;
import com.total.webecommerce.entity.projection.OfUser.ProductInfo;
import com.total.webecommerce.entity.support.PaymentStatus;
import com.total.webecommerce.respository.OfProduct.ProductRepository;
import com.total.webecommerce.respository.OfUser.AccountBankRepository;
import com.total.webecommerce.respository.OfUser.FavoritesRepository;
import com.total.webecommerce.respository.OfUser.UserRepository;
import com.total.webecommerce.respository.OrOrder.OrderBillRepository;
import com.total.webecommerce.respository.OrOrder.OrderItemRepository;
import com.total.webecommerce.respository.OrOrder.PaymentRepository;
import com.total.webecommerce.resquest.OfOrder.AddProductToOrder;
import com.total.webecommerce.resquest.OfOrder.ChangeProductFromOrder;
import com.total.webecommerce.resquest.OfOrder.RemoveOrderItem;
import com.total.webecommerce.resquest.OfUser.CreatePaymentResquest;
import org.springframework.stereotype.Service;
import com.total.webecommerce.exception.BadResquestException;
import com.total.webecommerce.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

@Service
@Slf4j
public class OrderSerivce {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FavoritesRepository favorites;
    @Autowired
    private OrderBillRepository orders;
    @Autowired
    private OrderItemRepository orderItem;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private AccountBankRepository accountBankRepository;
    public OrderBillInfo getMyOrder(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        Optional<OrderBillInfo> bill = orders.findByUser(user.getId());
        if (bill.isEmpty()) {
            throw new BadResquestException("Hiện tại chưa có SP nào trong giỏ hàng !! ");
        }
        bill.get().getOrderItems().stream().sorted((o1, o2) ->o1.getId().compareTo(o2.getId()));
        return bill.get();
    }

    public List<ProductInfo> getProductInfo(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        List<OrderBill> bills = orders.findByUser_Email(user.getEmail());
        if (bills == null) {
            throw new NotFoundException("Không tìm thấy Bill của User này ");
        }
        List<Integer> brandIds = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        for (OrderItem e : bills.get(0).getOrderItems()) {
            ids.add(e.getProduct().getId());
            brandIds.add(e.getProduct().getBrand().getId());
        }
        return productRepository.findByBrand_IdAndIdNotIn(brandIds.get(0), ids).subList(0, 4);
    }

    public ResponseEntity<?> addProductToOrder(AddProductToOrder resquest) {
        Optional<User> user = userRepository.findByEmail(resquest.getEmail());
        Optional<Product> product = productRepository.findById(resquest.getProductId());
        if (orders.findByUser_Id(user.get().getId()) == null) {
            OrderItem item = OrderItem.builder()
                    .product(product.get())
                    .nums(resquest.getNums())
                    .build();
            Set<OrderItem> items = new LinkedHashSet<>();
            items.add(item);
            orderItem.save(item);
            log.info("Vào Đk 1  : " + item.getPrice());
            OrderBill orderOfUser = new OrderBill();
            orderOfUser.setUser(user.get());
            orderOfUser.setOrderItems(items);
            orderOfUser.setStatus(true);
            log.info("Vào Đk 1 : " + orderOfUser.getOrderItems());
            orders.save(orderOfUser);
            return ResponseEntity.ok("Thêm sản phẩm vào giỏ hàng thành công !! ");
        }
        OrderBill order = orders.findByUser_Id(user.get().getId());
        int check = 0;
        for (OrderItem item : order.getOrderItems()) {
            if (item.getProduct().getId() == resquest.getProductId()) {
                if (item.getNums() + resquest.getNums() > product.get().getNums()) {
                    throw new BadResquestException("Số lượng sản phẩm tại cửa hàng không đủ ");
                }
                item.setNums(item.getNums() + resquest.getNums());
                orderItem.save(item);
                check += 1;
            }
        }
        if (check > 0) {
            return ResponseEntity.ok("Thêm Sp vào cửa hàng thành công");
        }
        if (resquest.getNums() > product.get().getNums()) {
            throw new BadResquestException("Số lượng sản phẩm tại Cửa hàng không đủ ");
        }
        OrderItem itemNew = OrderItem.builder()
                .product(product.get())
                .nums(resquest.getNums())
                .build();
        orderItem.save(itemNew);
        order.getOrderItems().add(itemNew);
        log.info("Truocw khi luu");
        orders.save(order);
        return ResponseEntity.ok("Thêm thành công sản phẩm vào giỏ hàng");
    }
    public ResponseEntity<?> getOrderById(Integer orderId){
        return ResponseEntity.accepted().body(orders.findByBill_Id(orderId));
    }
    public ResponseEntity<?> removeProductFromOrder(RemoveOrderItem resquest){
        log.info("Remove Product Step 1 ");
        Optional<User> user = userRepository.findByEmail(resquest.getEmail());
        Optional<Product> product = productRepository.findById(resquest.getProductId());
        log.info("Remove Product Step 2 ");
        OrderBill order = orders.findByUser_Id(user.get().getId());
        log.info("Remove Product Step 3 ");
        for (OrderItem item : order.getOrderItems()){
            log.info("Remove Product Step 4 ");
            if(item.getProduct().getId() == product.get().getId()){
                log.info("Remove Product Step 5 ");
                order.getOrderItems().remove(item);
                orderItem.delete(item);
                orders.save(order);
            }
        }
        log.info("Remove Product Step 6 ");
        if(order.getOrderItems().isEmpty()){

            orders.delete(order);
            return ResponseEntity.ok("Hiện tại không còn SP nào trong giỏ hàng của bạn !! ");
        }
        log.info("Remove Product Step 7");
        return ResponseEntity.ok("Đã xóa sản phẩm khỏi giỏ hàng của bạn !! ");
    }

    // thêm số lượng tại giỏ hàng của SP
    public  ResponseEntity<?> changeProductFormOrder(ChangeProductFromOrder resquest) {
        OrderItem item = orderItem.findById(resquest.getItemId()).get();
        item.setNums(resquest.getNums());
        orderItem.save(item);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Thêm thành công !! ");
    }

    public ResponseEntity<?> createPayment(CreatePaymentResquest resquest){
        log.info("Vào Đây  : " + resquest);
        User user = userRepository.findById(resquest.getUserId()).get();
        OrderBill orderBill = orders.findById(resquest.getOrderId()).get();
        for (OrderItem e : orderBill.getOrderItems()){
            if(e.getNums() > e.getProduct().getNums()){
                throw new BadResquestException("Số lượng sản phẩm tại cửa hàng không đủ !! ");
            }
        }
        Payment payment = new Payment();
        payment.setUser(user);
        payment.setOrderBill(orderBill);
        payment.setPrice(resquest.getTotalPrice());
        payment.setAddress(resquest.getAddress());
        payment.setText(payment.getText());
        payment.setTransport(resquest.getTransport());
        payment.setPaymentStatus(PaymentStatus.INITIAL);
        payment.setType(resquest.getCategoryPayment());
        if(resquest.getCategoryPayment() == 0){
            payment.setAccountBank(null);
        }else {
            AccountBank acc = new AccountBank();
            acc.setNameAccount(resquest.getNameAccount());
            acc.setBankBranch(resquest.getBrandBank());
            acc.setNumberAccount(resquest.getNumberAccount());
            payment.setAccountBank(acc);
            accountBankRepository.save(acc);
        }
        orderBill.setStatus(false);
        orders.save(orderBill);
        paymentRepository.save(payment);
        return ResponseEntity.ok("Tạo đơn hàng thành công !! ");
    }

    public List<PaymentInfo> getMyBill(String email){
        return paymentRepository.findByUser_EmailAndPaymentStatus(email,PaymentStatus.INITIAL,PaymentStatus.PROCEED);
    }

    public PaymentInfo getPaymentById(Integer paymentId) {
        return paymentRepository.findById_Payment(paymentId);
    }
}
