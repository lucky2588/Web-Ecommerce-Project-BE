package com.total.webecommerce.respository.OfAdmin;

import com.total.webecommerce.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepository extends JpaRepository<Discount, Integer> {
}