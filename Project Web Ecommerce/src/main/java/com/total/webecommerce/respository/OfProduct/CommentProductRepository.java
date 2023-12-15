package com.total.webecommerce.respository.OfProduct;

import com.total.webecommerce.entity.CommentProduct;
import com.total.webecommerce.entity.projection.Public.CommentProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentProductRepository extends JpaRepository<CommentProduct, Integer> {
    Page<CommentProductInfo> findByProduct_Id(Pageable pageable, Integer id);

    @Query("select c from CommentProduct c where c.product.id = ?1 order by c.id DESC")
    Page<CommentProductInfo> findByProduct_IdOrderByIdDesc(Integer id, Pageable pageable);



}