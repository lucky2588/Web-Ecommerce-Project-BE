package com.total.webecommerce.resquest.OfBlog;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBlogResquest {
    private Integer blogId;
    @NotEmpty(message = "Đây là trường không được để trống ")
    private String title;
    @NotEmpty(message = "Đây là trường không được để trống ")
    private String content;
    @NotEmpty(message = "Đây là trường không được để trống ")
    private String description;
    private Integer brandId;
    private Integer publicOf;
}
