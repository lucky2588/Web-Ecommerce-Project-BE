package com.total.webecommerce.respository.OrOrder;

import com.total.webecommerce.entity.dto.BestBuyer;
import com.total.webecommerce.entity.dto.ProductBestSeller;
import com.total.webecommerce.entity.projection.OfUser.TotalPaymentInfo;
import com.total.webecommerce.entity.Payment;
import com.total.webecommerce.entity.projection.OfUser.PaymentInfo;
import com.total.webecommerce.entity.support.PaymentStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    @Query("select p from Payment p where p.user.id = ?1 and (p.paymentStatus = ?2 or  p.paymentStatus = ?3)")
    List<PaymentInfo> findByUser_IdlAndPaymentStatus(Integer userId, PaymentStatus paymentStatus, PaymentStatus paymentStatus1);

    @Query("""
            select p from Payment p
            where p.user.id = ?1 and ( p.paymentStatus = ?2 or p.paymentStatus = ?3 or p.paymentStatus = ?4)
            order by p.id DESC""")
    Page<PaymentInfo> findByUser_IdAndPaymentStatusOrPaymentStatusOrderByIdDesc(Integer id, PaymentStatus paymentStatus, PaymentStatus paymentStatus1,PaymentStatus paymentStatus2, Pageable pageable);

    @Query("""
            select p from Payment p
            where p.user.id = ?1 and ( p.paymentStatus = ?2 or p.paymentStatus = ?3 or p.paymentStatus = ?4)
            order by p.id DESC""")
    List<PaymentInfo> findPaymentsByUserId(Integer id, PaymentStatus paymentStatus, PaymentStatus paymentStatus1,PaymentStatus paymentStatus2);



    @Query("select p from Payment p where p.user.id = ?1 and p.paymentStatus = ?2")
    List<Payment> findByUser_IdAndPaymentStatus(Integer id, PaymentStatus paymentStatus);

    @Query("select p from Payment p where p.paymentStatus = ?1 and p.createAt > ?2 order by p.id DESC")
    Page<PaymentInfo> findByPaymentStatusOrderByIdDesc(Pageable pageable,PaymentStatus paymentStatus, LocalDate time);
    @Query("select p from Payment p order by p.id DESC")
    Page<PaymentInfo> findPayments(Pageable pageable);
    @Query("select p from Payment p where p.paymentStatus= ?1 order by p.id DESC")
    Page<PaymentInfo> findPayments(PaymentStatus paymentStatus,Pageable pageable);
    @Query("select p from Payment p where  p.createAt > ?1 order by p.id DESC")
    Page<PaymentInfo> findAllOrderByTime(LocalDate createAt,Pageable pageable);

    @Query("select p from Payment p where p.createAt > ?1 order by p.id DESC")
    Page<PaymentInfo> findByCreateAtGreaterThanOrderByIdAsc(LocalDate createAt, Pageable pageable);




    @Query("select p from Payment p where  p.id = ?1")
    PaymentInfo findById_Payment(Integer id);

    @Query("select p from Payment p where p.user.id = ?1 and p.paymentStatus in ?2")
    List<Payment> findByUser_IdAndPaymentStatusIn(Integer id, Collection<PaymentStatus> paymentStatuses);

    @Query("select p from Payment p Where p.paymentStatus = ?1 ")
    List<PaymentInfo> findByPaymentStatus(PaymentStatus paymentStatus);

    @Query("select p from Payment p where p.paymentStatus = ?1")
    List<PaymentInfo> getPayment(PaymentStatus paymentStatus);
    @Query("select p from Payment p where p.paymentStatus = ?1")
    List<Payment> getPaymentByStatus(PaymentStatus paymentStatus);





    @Query("select p from Payment p where p.createAt > ?1 order by p.id desc")
    List<PaymentInfo> findByCreateAt(LocalDate createAt);




    //    @Query(nativeQuery = true , value = " SELECT Sum(price) FROM payment a")
//    TotalPayment getPayment();
    @Query(nativeQuery = true, value = " SELECT SUM(a.nums_sold) AS Price FROM product a")
    TotalPaymentInfo getProductBuy();


    @Query(nativeQuery = true,value ="SELECT a.product_id ,SUM(a.price), SUM(a.nums),d.thumbail " +
            "FROM (((order_item a JOIN order_bill b ON a.order_bill_id = b.id) LEFT JOIN product d ON a.product_id = d.id)) RIGHT JOIN payment c ON b.id = c.order_bill_id" +
            " WHERE b.`status` = 0 AND c.payment_status = :enumValue " +
            "GROUP BY  a.product_id")
    List<ProductBestSeller> getProductBestSeller(@Param("enumValue")String status);

    @Query("select new com.total.webecommerce.entity.dto.BestBuyer" +
            "(a.user.id ,b.name, COUNT(b.id), b.avatar, SUM(a.price) ) "+
            " from Payment a LEFT JOIN User b ON a.user.id = b.id " +
            "where a.paymentStatus = ?1 " +
            "GROUP BY a.user.id " +
            "ORDER BY SUM(a.price) DESC")
    List<BestBuyer> getBuyer(PaymentStatus paymentStatus);





}
