package com.total.webecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Getter
@Slf4j
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_bill")
public class OrderBill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "totalPrice")
    private Double TotalPrice;
    @Column(name = "status")
    private Boolean status;
    @ManyToOne(cascade = CascadeType.DETACH,fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "order_bill_id")
    private Set<OrderItem> orderItems = new LinkedHashSet<>();
    @PrePersist
    public void PrePersist(){
        this.TotalPrice = 0.0;
        this.status = true;
        if(this.orderItems != null ){
            for (OrderItem e : orderItems){
                this.TotalPrice += (double) e.getPrice();
            }
        }
    }
    @PreRemove
    public void PreRemove(){
        for (OrderItem item : this.orderItems){
            orderItems.remove(item);
        }
        this.setOrderItems(null);
        setStatus(false);
    }


    @Modifying
    @PostUpdate
    public void PostUpdate(){
        this.TotalPrice = 0.0;
        if(this.orderItems.isEmpty()){
            this.status = false;
        }
        log.info("Size cuar List "+this.orderItems.size());
        if(!this.orderItems.isEmpty()){

            Double longTotal=0.0;
            for (OrderItem e : this.orderItems){
                longTotal += (double) e.getPrice();
            }
            this.TotalPrice = longTotal;
        }else{
            this.status = false;
        }
    }

    public Double getTotalPrice() {
        return getOrderItems().stream().mapToDouble(OrderItem::getPrice).sum();
    }
}