package com.total.webecommerce.service;

import com.total.webecommerce.entity.*;
import com.total.webecommerce.entity.projection.OfUser.OrderBillInfo;
import com.total.webecommerce.entity.projection.OfUser.PaymentInfo;
import com.total.webecommerce.entity.projection.OfUser.ProductInfo;
import com.total.webecommerce.entity.support.NotificationStatus;
import com.total.webecommerce.entity.support.PaymentStatus;
import com.total.webecommerce.respository.OfAdmin.NotificationRepository;
import com.total.webecommerce.respository.OfAdmin.RoleRepository;
import com.total.webecommerce.respository.OfProduct.ProductRepository;
import com.total.webecommerce.respository.OfUser.AccountBankRepository;
import com.total.webecommerce.respository.OfUser.FavoritesRepository;
import com.total.webecommerce.respository.OfUser.UserRepository;
import com.total.webecommerce.respository.OrOrder.OrderBillRepository;
import com.total.webecommerce.respository.OrOrder.OrderItemRepository;
import com.total.webecommerce.respository.OrOrder.PaymentRepository;
import com.total.webecommerce.resquest.OfOrder.AddProductToOrder;
import com.total.webecommerce.resquest.OfOrder.ChangeProductFromOrder;
import com.total.webecommerce.resquest.OfOrder.ReasonCancle;
import com.total.webecommerce.resquest.OfOrder.RemoveOrderItem;
import com.total.webecommerce.resquest.OfUser.CreatePaymentResquest;
import com.total.webecommerce.security.iCurrent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import com.total.webecommerce.exception.BadResquestException;
import com.total.webecommerce.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class OrderSerivce {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private iCurrent iCurrent;
    @Autowired
    private NotificationRepository notificationRepository;
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

    public OrderBillInfo getMyOrder(Integer userId) { // lấy ra sản phẩm trong giỏ hàng của User
        User user = userRepository.findById(userId).orElse(null);
        Optional<OrderBillInfo> bill = orders.findByUser(user.getId());
        if (bill.isEmpty()) {
            throw new BadResquestException("Hiện tại chưa có SP nào trong giỏ hàng !! ");
        }
//        bill.get().getOrderItems().stream().sorted((o1, o2) ->o1.getId().compareTo(o2.getId()));
        return bill.get();
    }

    public List<ProductInfo> getProductInfo(Integer userId) { // lấy ra các sp tương tự trong khi thanh toán
        User user = userRepository.findById(userId).orElse(null);
        List<OrderBill> bills = orders.findByProductInfo(user.getId());
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
                Product product1 = productRepository.findById(item.getProduct().getId()).orElseThrow(
                        ()-> {
                            throw new NotFoundException("Không tìm thấy Sản phẩm này !! ");
                        }
                );
                item.setNums(item.getNums() + resquest.getNums());
                if(product1.getDiscount() == null){
                    item.setPrice((double) (product1.getPrice() * item.getNums()));
                }else{
                    item.setPrice((double) (product1.getSales() * item.getNums()));
                }
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
       Product product1 = productRepository.findById(itemNew.getProduct().getId()).orElseThrow(
               ()->{
                   throw new NotFoundException("Không tìm thấy Sản phẩm này !! ");
               }
       );
        if(product1.getDiscount() == null){
            itemNew.setPrice((double) (product1.getPrice() * itemNew.getNums()));
        }else{
            itemNew.setPrice((double) (product1.getSales() * itemNew.getNums()));
        }
        orderItem.save(itemNew);
        order.getOrderItems().add(itemNew);
        orders.save(order);
        return ResponseEntity.ok("Thêm thành công sản phẩm vào giỏ hàng");
    }

    public ResponseEntity<?> getOrderById(Integer orderId) {
        return ResponseEntity.accepted().body(orders.findByBill_Id(orderId));
    }

    public ResponseEntity<?> removeProductFromOrder(RemoveOrderItem resquest) {
        Optional<User> user = userRepository.findByEmail(resquest.getEmail());
        Optional<Product> product = productRepository.findById(resquest.getProductId());
        OrderBill order = orders.findByUser_Id(user.get().getId());
        for (OrderItem item : order.getOrderItems()) {
            if (item.getProduct().getId() == product.get().getId()) {
                order.getOrderItems().remove(item);
                orderItem.delete(item);
                orders.save(order);
            }
        }
        if (order.getOrderItems().isEmpty()) {
            orders.delete(order);
            return ResponseEntity.ok("Hiện tại không còn SP nào trong giỏ hàng của bạn !! ");
        }
        return ResponseEntity.ok("Đã xóa sản phẩm khỏi giỏ hàng của bạn !! ");
    }

    // thêm số lượng tại giỏ hàng của SP
    public ResponseEntity<?> changeProductFormOrder(ChangeProductFromOrder resquest) {
        OrderItem item = orderItem.findById(resquest.getItemId()).get();
        Product product = productRepository.findById(item.getProduct().getId()).orElseThrow(
                ()-> {
                    throw new NotFoundException("Không tìm thấy Product with Id "+item.getProduct().getId());
                }
        );
        item.setNums(resquest.getNums());
        if(product.getDiscount() == null){
          item.setPrice((double) (product.getPrice() * resquest.getNums()));
        }else{
            item.setPrice((double) (product.getSales() * resquest.getNums()));
        }
        orderItem.save(item);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Thêm thành công !! ");
    }

    public ResponseEntity<?> createPayment(CreatePaymentResquest resquest) {

        User user = userRepository.findById(resquest.getUserId()).get();
        OrderBill orderBill = orders.findById(resquest.getOrderId()).get();
        for (OrderItem e : orderBill.getOrderItems()) {
            if (e.getNums() > e.getProduct().getNums()) {
                throw new BadResquestException("Số lượng sản phẩm tại cửa hàng không đủ !! ");
            }
        }
        Payment payment = new Payment();
        payment.setUser(user);
        payment.setOrderBill(orderBill);
        payment.setPrice(resquest.getTotalPrice());
        payment.setAddress(resquest.getAddress());
        payment.setText(resquest.getNote());
        payment.setTransport(resquest.getTransport());
        payment.setPaymentStatus(PaymentStatus.INITIAL);
        payment.setType(resquest.getCategoryPayment());
        if (resquest.getCategoryPayment() == 0) {
            payment.setAccountBank(null);
        } else {
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
        String accOfUSer = user.getName();
        Notification notification = Notification.builder()
                .username(accOfUSer)
                .title("New Order")
                .avatar(user.getAvatar())
                .content(accOfUSer + "  just placed a new order with ID : " + payment.getId())
                .notificationStatus(NotificationStatus.ORDERS)
                .typeOf(0)
                .build();
        notificationRepository.save(notification);
        return ResponseEntity.ok("Tạo đơn hàng thành công !! ");
    }

    public List<PaymentInfo> getMyBill(Integer userId) {
        List<PaymentInfo> list = paymentRepository.findByUser_IdlAndPaymentStatus(userId, PaymentStatus.INITIAL, PaymentStatus.PROCEED);
        if (list.isEmpty()) {
            throw new NotFoundException("Không có đơn hàng nào của bạn ");
        }
        return list;
    }
    public List<PaymentInfo> getPaymentsForAdmin(Integer userId) {
        List<PaymentInfo> list = paymentRepository.findPaymentsByUserId(userId, PaymentStatus.SUCCESS, PaymentStatus.REFUND,PaymentStatus.CANCLE);
        if (list.isEmpty()) {
            throw new NotFoundException("Không có đơn hàng nào của bạn ");
        }
        return list;
    }
    public Page<PaymentInfo> getPayments(Integer userId , Integer page , Integer pageSize) {
        Page<PaymentInfo> list = paymentRepository.findByUser_IdAndPaymentStatusOrPaymentStatusOrderByIdDesc(userId, PaymentStatus.SUCCESS, PaymentStatus.REFUND,PaymentStatus.CANCLE,PageRequest.of(page,pageSize));
        if (list.getContent().isEmpty()) {
            throw new NotFoundException("Không có đơn hàng nào của bạn ");
        }
        return list;
    }
    public PaymentInfo getPaymentById(Integer paymentId) {
        return paymentRepository.findById_Payment(paymentId);
    }
    public ResponseEntity<?> deletePaymentByUser(Integer paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(
                () -> {
                    throw new NotFoundException("Không tìm thấy đơn hàng với ID là " + paymentId);
                }
        );

        if (payment.getPaymentStatus() != PaymentStatus.INITIAL) {
            throw new BadResquestException("Không thể hủy đơn hàng này !! ");
        }
        if (payment.getAccountBank() != null) {
            AccountBank acc = accountBankRepository.findById(payment.getAccountBank().getId()).orElseThrow(
                    () -> {
                        throw new NotFoundException("Không tìm thấy acc này ");
                    }
            );
            accountBankRepository.delete(acc);
        }
        log.info(" " + payment.getOrderBill().getId());
        OrderBill orderBill = orders.findById(payment.getOrderBill().getId()).orElseThrow(
                () -> {
                    throw new NotFoundException("Không tìm thấy Order ");
                }
        );
        paymentRepository.delete(payment);
        orderItem.deleteAllByIdIn(orderBill.getOrderItems().stream().map(t -> t.getId()).collect(Collectors.toSet()));
        orders.deleteById(orderBill.getId());
        return ResponseEntity.ok("Delete  Seccess !! ");
    }
    public void deleleItem() {
        List<OrderItem> items = orderItem.findOrderItemIsNull();
        for (OrderItem item : items) {
            orderItem.delete(item);
        }
    }

    // service for Admin
    public List<PaymentInfo> getPayments() {
        List<PaymentInfo> PaymentList = paymentRepository.findByCreateAt(LocalDate.now().minusDays(3));
        if (PaymentList.isEmpty()) {
            throw new NotFoundException("Not found order this day ");
        }
        return PaymentList;
    }

    public Page<PaymentInfo> getOrderAll(Integer page, Integer pageSize, Integer choose, Integer time) {
        if (choose == 1) {
            if (time == 1) {
                return paymentRepository.findPayments(PageRequest.of(page, pageSize));
            }
            if (time == 2) {
                return paymentRepository.findByCreateAtGreaterThanOrderByIdAsc(LocalDate.now().minusDays(7), PageRequest.of(page, pageSize));
            }
            if (time == 3) {
                return paymentRepository.findByCreateAtGreaterThanOrderByIdAsc(LocalDate.now().minusDays(31), PageRequest.of(page, pageSize));
            }
        }
        if (choose == 2) {
            if (time == 1) {
                return paymentRepository.findPayments(PaymentStatus.SUCCESS, PageRequest.of(page, pageSize));
            }
            if (time == 2) {
                return paymentRepository.findByPaymentStatusOrderByIdDesc(PageRequest.of(page, pageSize), PaymentStatus.SUCCESS, LocalDate.now().minusWeeks(1));
            }
            if (time == 3) {
                return paymentRepository.findByPaymentStatusOrderByIdDesc(PageRequest.of(page, pageSize), PaymentStatus.SUCCESS, LocalDate.now().minusMonths(1));
            }
        }
        if (choose == 3) {
            if (time == 1) {
                return paymentRepository.findPayments(PaymentStatus.INITIAL, PageRequest.of(page, pageSize));
            }
            if (time == 2) {
                return paymentRepository.findByPaymentStatusOrderByIdDesc(PageRequest.of(page, pageSize), PaymentStatus.INITIAL, LocalDate.now().minusWeeks(1));
            }
            if (time == 3) {
                return paymentRepository.findByPaymentStatusOrderByIdDesc(PageRequest.of(page, pageSize), PaymentStatus.INITIAL, LocalDate.now().minusMonths(1));
            }
        }
        if (choose == 4) {
            if (time == 1) {
                return paymentRepository.findPayments(PaymentStatus.PROCEED, PageRequest.of(page, pageSize));
            }
            if (time == 2) {
                return paymentRepository.findByPaymentStatusOrderByIdDesc(PageRequest.of(page, pageSize), PaymentStatus.PROCEED, LocalDate.now().minusWeeks(1));
            }
            if (time == 3) {
                return paymentRepository.findByPaymentStatusOrderByIdDesc(PageRequest.of(page, pageSize), PaymentStatus.PROCEED, LocalDate.now().minusMonths(1));
            }
        }
        if (choose == 5) {
            if (time == 1) {
                return paymentRepository.findPayments(PaymentStatus.CANCLE, PageRequest.of(page, pageSize));
            }
            if (time == 2) {
                return paymentRepository.findByPaymentStatusOrderByIdDesc(PageRequest.of(page, pageSize), PaymentStatus.CANCLE, LocalDate.now().minusDays(7));
            }
            if (time == 3) {
                return paymentRepository.findByPaymentStatusOrderByIdDesc(PageRequest.of(page, pageSize), PaymentStatus.CANCLE, LocalDate.now().minusDays(31));
            }
        }
        return paymentRepository.findPayments(PageRequest.of(page, pageSize));
    }

    public ResponseEntity<?> accpectPayment(Integer paymentId){
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(
                () -> {
                    throw new NotFoundException("Not found Payment with Id " + paymentId);
                }
        );
        if (payment.getPaymentStatus() != PaymentStatus.INITIAL) {
            throw new BadResquestException("Can not Accpect this Payment with Id" + paymentId);
        }
        for (OrderItem item : payment.getOrderBill().getOrderItems()) {
            Product product = productRepository.findById(item.getProduct().getId()).orElseThrow(
                    () -> {
                        throw new NotFoundException("Not found Product with ID" + item.getProduct().getId());
                    }
            );
            if (item.getNums() > product.getNums() - 1) {
                throw new BadResquestException("Not enough nums of product ");
            }
            product.setNums(product.getNums() - item.getNums());
            productRepository.save(product);
        }
        payment.setPaymentStatus(PaymentStatus.PROCEED);
        payment.setDelivery(LocalDate.now());
        paymentRepository.save(payment);
        User accOfAdmin = iCurrent.getUser();
        Notification notification = Notification.builder()
                .username(accOfAdmin.getName())
                .title("New Order")
                .avatar(accOfAdmin.getAvatar())
                .content(accOfAdmin.getName() + "  Accpect  a new order with ID : " + payment.getId())
                .notificationStatus(NotificationStatus.ORDERS)
                .typeOf(1)
                .build();
        notificationRepository.save(notification);
        return ResponseEntity.ok("Accpect Payment Seccess !! ");
    }
    public ResponseEntity<?> cancleOrder(Integer paymentId,ReasonCancle reason){
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(
                () -> {
                    throw new NotFoundException("Not found Payment with ID " + paymentId);
                }
        );
        if (payment.getPaymentStatus() == PaymentStatus.SUCCESS) {
            throw new BadResquestException("Can not Cancle this Order !! ");
        }
        payment.setPaymentStatus(PaymentStatus.CANCLE);
        payment.setReasonCancle(reason.getNote());
        payment.setReceived(LocalDate.now());
        User user  = iCurrent.getUser();

            Notification notification = Notification.builder()
                    .username(user.getName())
                    .title("Cancle Order")
                    .avatar(user.getAvatar())
                    .content(user.getName() + " Cancle  Order with ID : " + payment.getId())
                    .notificationStatus(NotificationStatus.ORDERS)
                    .typeOf(1)
                    .build();
            notificationRepository.save(notification);
        paymentRepository.save(payment);
        return ResponseEntity.ok("Cancle Order seccess !! ");
    }
    public ResponseEntity<?> notReceiveOrder(Integer paymentId,ReasonCancle reason){
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(
                () -> {
                    throw new NotFoundException("Not found Payment with ID " + paymentId);
                }
        );
        if (payment.getPaymentStatus() == PaymentStatus.SUCCESS) {
            throw new BadResquestException("Can not Cancle this Order !! ");
        }
        if (payment.getPaymentStatus() != PaymentStatus.PROCEED) {
            throw new BadResquestException("Can not Cancle this Order !! ");
        }
        payment.setPaymentStatus(PaymentStatus.Not_Receive);
        payment.setReasonCancle(reason.getNote());
        payment.setReceived(LocalDate.now());
        User user  = payment.getUser();
        Notification notification = Notification.builder()
                .username(user.getName())
                .title("Cancle Order")
                .avatar(user.getAvatar())
                .content(user.getName() + " Cancle  Order with ID : " + payment.getId())
                .notificationStatus(NotificationStatus.ORDERS)
                .typeOf(0)
                .build();
        notificationRepository.save(notification);
        paymentRepository.save(payment);
        return ResponseEntity.ok("Cancle Order seccess !! ");
    }


    public ResponseEntity<?> refundOrder(Integer paymentId,ReasonCancle reasonCancle) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(
                () -> {
                    throw new NotFoundException("Not found Payment with ID " + paymentId);
                }
        );
        if(payment.getPaymentStatus() != PaymentStatus.PROCEED){
            throw new BadResquestException("Can Refund this Order with Id "+paymentId);
        }
        if (payment.getPaymentStatus() == PaymentStatus.SUCCESS){
            throw new BadResquestException("Can not Refund this Order !! ");
        }
        for (OrderItem item : payment.getOrderBill().getOrderItems()) {
            Product product = productRepository.findById(item.getProduct().getId()).orElseThrow(
                    () -> {
                        throw new NotFoundException("Not found product with Id" + item.getProduct().getId());
                    }
            );
            product.setNums(product.getNums() + item.getNums());
            productRepository.save(product);
        }
        if(payment.getReasonCancle() == null){
            payment.setReasonCancle(reasonCancle.getNote());
        }
        payment.setPaymentStatus(PaymentStatus.REFUND);
        payment.setReceived(LocalDate.now());
        paymentRepository.save(payment);
        User user = iCurrent.getUser();
        Notification notification = Notification.builder()
                .username(user.getName())
                .title("Refund Order")
                .avatar(user.getAvatar())
                .content(user.getName() + " Refund  Order with ID : " + payment.getId())
                .notificationStatus(NotificationStatus.ORDERS)
                .typeOf(1)
                .build();
        notificationRepository.save(notification);
        return ResponseEntity.ok("Refund Order");
    }

    public ResponseEntity<?> receiveOrder(Integer paymentId){ //
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(
                () -> {
                    throw new NotFoundException("Not found Payment with ID " + paymentId);
                }
        );
        if (payment.getPaymentStatus() != PaymentStatus.PROCEED) {
            throw new BadResquestException("Can not Treatment for this Order !! ");
        }
        payment.setReceived(LocalDate.now());
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        paymentRepository.save(payment);
        User user  = iCurrent.getUser();
        Notification notification = Notification.builder()
                .username(user.getName())
                .title("Receive Order Seccess ")
                .avatar(user.getAvatar())
                .content(user.getName() + " Receive  Order with ID : " + payment.getId())
                .notificationStatus(NotificationStatus.ORDERS)
                .typeOf(0)
                .build();
        notificationRepository.save(notification);
        return ResponseEntity.ok(" Seccess !! ");
    }
    public ResponseEntity<?> returnOrder(Integer paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(
                () -> {
                    throw new NotFoundException("Not found Payment with ID " + paymentId);
                }
        );
        if (payment.getPaymentStatus() == PaymentStatus.SUCCESS){
            throw new BadResquestException("Can not Refund this Order !! ");
        }
        payment.setPaymentStatus(PaymentStatus.PROCEED);
        payment.setReceived(null);
        payment.setReasonCancle(null);
        payment.setDelivery(LocalDate.now());
        paymentRepository.save(payment);
        User user = iCurrent.getUser();
        Notification notification = Notification.builder()
                .username(user.getName())
                .title("Return Order")
                .avatar(user.getAvatar())
                .content(user.getName() + " Return  Order with ID : " + payment.getId())
                .notificationStatus(NotificationStatus.ORDERS)
                .typeOf(1)
                .build();
        notificationRepository.save(notification);
        return ResponseEntity.ok("Return Order");

    }


}
