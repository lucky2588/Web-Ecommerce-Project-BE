package com.total.webecommerce.respository.OrOrder;

import com.total.webecommerce.entity.OrderBill;
import com.total.webecommerce.entity.projection.OfUser.OrderBillInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderBillRepository extends JpaRepository<OrderBill, Integer> {
    @Query("select o from OrderBill o where o.user.id = ?1 and o.status = true")
    Optional<OrderBillInfo> findByUser(Integer id);
    @Query("select o from OrderBill o where o.user.id = ?1 and o.status = true")
     OrderBill findByUser_Id(Integer id);
    List<OrderBill> findByUser_Email(String email);

    @Query("select o from OrderBill o where o.id = ?1")
    OrderBillInfo findByBill_Id(Integer id);




}