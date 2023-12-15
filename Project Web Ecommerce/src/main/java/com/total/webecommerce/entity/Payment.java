package com.total.webecommerce.entity;
import com.total.webecommerce.entity.support.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "address")
    private String address;
    @Column(name = "transport")
    private Integer transport;
    @Column(name = "type")
    private Integer type;
    @Column(name = "text")
    private String text;
    @Column(name = "reasonCancle")
    private String reasonCancle;
    @Column(name = "thumbail")
    private String thumbail;
    @Column(name = "price")
    private Double price;
    @Column(name = "createAt")
    private LocalDate createAt;
    @Column(name = "delivery")
    private LocalDate delivery;
    @Column(name = "received")
    private LocalDate received;
    @Column(name = "paymentStatus")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_bill_id",unique = true)
    private OrderBill orderBill;
    @OneToOne
    @JoinColumn(name = "account_bank_id")
    private AccountBank accountBank;

    @PrePersist
    public void PrePersist(){
        this.setCreateAt(LocalDate.now());
        if(orderBill != null){
            this.thumbail = orderBill.getOrderItems().stream().findFirst().get().getProduct().getThumbail();
        }
    }

    @PreUpdate
    public void PreUpdate(){
        if(orderBill != null){
            this.thumbail = orderBill.getOrderItems().stream().findFirst().get().getProduct().getThumbail();
        }
    }
    @PreRemove
    public void PreRemove(){
        this.setUser(null);
        this.setOrderBill(null);
        this.setAccountBank(null);
    }

}