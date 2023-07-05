package com.total.webecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comment_product")
public class CommentProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name ="content")
    private String content;
    @Column(name ="createAt")
    private LocalDateTime createAt;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "product_id")
    private Product product;

    @PrePersist
    void PrePersits(){
        this.createAt = LocalDateTime.now();
    }

    @PreRemove
    public void PreRemove(){
        this.setUser(null);
        this.setProduct(null);
    }

}