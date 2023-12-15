package com.total.webecommerce.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.apache.catalina.mapper.Mapper;
import org.springframework.data.jpa.repository.Modifying;

import java.util.LinkedHashSet;
import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "nums")
    private Integer nums;
    @Column(name = "price")
    private Double price;
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "product_id")
    private Product product;
    @PrePersist
    public void PrePersist(){
        if (product == null) {
            return;
        }
        this.price = (double) (this.product.getPrice() * nums);
        if(product.getDiscount() != null){
            this.price = (double ) (this.product.getSales() * nums);
        }
    }
//    @PreUpdate
//    public void PreUpdate(){
//        if (product == null) {
//            return;
//        }
//        this.price =(double) (this.product.getPrice() * nums);
//        if(product.getDiscount() != null){
//            this.price = (double ) (this.product.getSales() * nums);
//        }
//    }
//    @PreRemove
//    public void PreRemove(){
//        this.setProduct(null);
//    }
}