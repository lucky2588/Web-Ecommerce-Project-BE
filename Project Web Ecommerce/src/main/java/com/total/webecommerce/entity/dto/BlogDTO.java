package com.total.webecommerce.entity.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 */
@Value
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class BlogDTO implements Serializable {
    Integer id;
    String title;
    String content;
    String thumbail;
    String description;
    Integer viewBlog;
    LocalDateTime createAt;
    Integer user_id;
    Integer brand_id;

}