package com.total.webecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "account_bank")
public class AccountBank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "nameAccout")
    private String nameAccount;
    @Column(name = "numberAccount")
    private String numberAccount;
    @Column(name = "bankBranch")
    private String bankBranch;
}