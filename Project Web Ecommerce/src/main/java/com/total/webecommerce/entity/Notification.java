package com.total.webecommerce.entity;
import com.total.webecommerce.entity.support.NotificationStatus;
import com.total.webecommerce.entity.support.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "username")
    private String username;
    @Column(name = "typeOf")
    private Integer typeOf;
    @Column(name = "title")
    private String title;
    @Column(name = "content")
    private String content;
    @Column(name = "notificationStatus")
    @Enumerated(EnumType.STRING)
    private NotificationStatus notificationStatus;
    @Column(name = "createAt")
    private LocalDate createAt;
    @PrePersist
    void PrePersist(){
        this.createAt = LocalDate.now();
    }
}