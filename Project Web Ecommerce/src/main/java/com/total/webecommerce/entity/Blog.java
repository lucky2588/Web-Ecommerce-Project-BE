package com.total.webecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "blog")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "title")
    private String title;
    @Column(name = "content")
    private String content;
    @Column(name = "thumbail")
    private String thumbail;
    @Column(name = "description")
    private String description;
    @Column(name = "statusBlog")
    private Integer statusBlog;
    @Column(name = "viewBlog")
    private  Integer viewBlog;
    @Column(name = "createAt")
    private LocalDateTime createAt;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.DETACH)
    @JoinColumn(name = "brand_id")
    private Brand brand;
    @JsonIgnore
    @OneToMany(mappedBy = "blog", cascade = CascadeType.REMOVE)
    private Set<CommentOfBlog> commentOfBlogs = new LinkedHashSet<>();

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    public void PrePersist() {
        this.createAt = LocalDateTime.now();
        this.viewBlog = 1;
    }


    @PreRemove
    void preRemove(){
        this.brand = null;
        this.user = null;
    }



}