package com.total.webecommerce.respository.OfAdmin;

import com.total.webecommerce.entity.Notification;
import com.total.webecommerce.entity.projection.OfAdmin.NotificationInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    @Query("select n from Notification n where n.typeOf = 0 order by  n.id DESC")
    Page<NotificationInfo> findByNouticationOfUser(Pageable pageable);

    @Query("select n from Notification n where n.typeOf = 1 order by n.id DESC")
    Page<NotificationInfo> findByNouticationOfAdmin(Pageable pageable);

    @Query("select n from Notification n order by n.id DESC")
    Page<NotificationInfo> findNoticationInfoAll(Pageable pageable);

    @Query("select n from Notification n where n.createAt > ?1 and n.typeOf = ?2 order by n.id desc ")
    List<NotificationInfo> findByCreateAtAndTypeOfOrderByCreateAtDesc(LocalDate createAt, Integer typeOf);







}