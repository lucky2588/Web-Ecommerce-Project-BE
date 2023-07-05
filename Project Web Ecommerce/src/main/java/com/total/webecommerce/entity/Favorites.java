package com.total.webecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "favorites")
public class Favorites {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinTable(name = "favorites_products",
            joinColumns = @JoinColumn(name = "favorites_id"),
            inverseJoinColumns = @JoinColumn(name = "products_id"))
    private Set<Product> products = new LinkedHashSet<>();

    @Column(name = "status")
    private Boolean status;

    @PrePersist
    void PrePersist(){
        if(products.isEmpty()){
            this.status = false;
        }
        this.status = true;
    }


    @PreRemove
    void PreRemove(){
        this.setUser(null);
        for (Product p : products){
            p = null;
        }
    }

}