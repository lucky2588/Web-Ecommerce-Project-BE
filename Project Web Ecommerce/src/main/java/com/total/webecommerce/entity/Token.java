package com.total.webecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "token")
public class Token {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id_token;
    @Column(name = "token")
    private String tokenUser;
    @Column(name = "createAt")
    private LocalDateTime createAt;
    @Column(name = "expiresAt")
    private LocalDateTime expiresAt;
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id")
    private User user;
    @PrePersist
    public void prePersist(){
        this.createAt = LocalDateTime.now();
        this.expiresAt = LocalDateTime.now().plusMinutes(2);
    }


}