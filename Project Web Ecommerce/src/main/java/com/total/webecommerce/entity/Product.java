package com.total.webecommerce.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.repository.Modifying;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name ="name")
    private String name;
    @Column(name ="price")
    private Double price;
    @Column(name ="thumbail")
    private String thumbail;
    @Type( JsonType.class) // đánh giấu kiểu List dưới dạng Jsson
    @Column(name ="listImage", columnDefinition = "json")
    private List<String> ListImage;
    @Column(name = "content")
    private String content;
    @Column(name = "detail")
    private String detail;
    @Column(name = "description")
    private String description;
    @Column(name = "nums") // số lượng spham ban đầu
    private Integer nums;
    @Column(name = "numsSold")
    private Integer numsSold; // số sp bán ra
    @Column(name = "view")
    private Integer view;
    @Column(name = "status")
    private Boolean status;
    @Column(name = "createAt")
    private LocalDateTime createAt;
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "discountId")
    private Discount discount;
    @Column(name = "sales")
    private Double sales;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<OrderItem> orderItems = new LinkedHashSet<>();

    @PrePersist
    void PrePersist(){
        this.createAt = LocalDateTime.now();
        this.view = 1;
        this.numsSold =0;
        this.sales = 0.0;
        if(numsSold == nums){
            this.status = false;
        }
//        if(this.thumbail.isEmpty()){
//            this.thumbail = this.ListImage.get(0);
//        }

        if(this.discount != null){
            Double numbers = (double)(this.getPrice() *(100.0 - discount.getPercent()))/100;
            double roundedNumber = Math.round(numbers * 100.0) / 100.0;
            this.setSales(roundedNumber);
        }
        this.status = true;
    }
    @PreRemove
    void PreRomove(){
        this.setDiscount(null);
        this.setCategory(null);
        this.setBrand(null);
    }
    @Modifying
    @PreUpdate
    void PreUpdate(){
        if(this.thumbail.isEmpty()){
            this.thumbail = this.ListImage.get(0);
        }
        this.sales = 0.0;
        if(this.discount != null){
            Double numbers = (double)(this.getPrice() *(100.0 - discount.getPercent()))/100;
            double roundedNumber = Math.round(numbers * 100.0) / 100.0;
            this.setSales(roundedNumber);
        }
        if(nums == 0){
            this.status = false;
        }
    }

    public Double getPrice() {
        return price == null ? 0D : price;
    }
}