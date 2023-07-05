package com.total.webecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comment_of_blog")
public class CommentOfBlog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "content")
    private String content;
    @Column(name = "createAt")
    private LocalDateTime createAt;
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "blog_id")
    private Blog blog;
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id")
    private User user;
    @PrePersist
    public void PrePersist(){
        this.createAt = LocalDateTime.now();
    }

    @PreRemove
    public void PreRemove(){
        this.blog = null;
        this.user = null;
    }

}