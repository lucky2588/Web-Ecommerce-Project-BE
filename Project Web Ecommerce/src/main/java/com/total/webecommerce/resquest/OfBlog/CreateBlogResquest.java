package com.total.webecommerce.resquest.OfBlog;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CreateBlogResquest {
    @NotEmpty(message = "Đây là trường không được để trống ")
    private String title;
    @NotEmpty(message = "Đây là trường không được để trống ")
    private String content;
    @NotEmpty(message = "Đây là trường không được để trống ")
    private String description;
    private Integer brandId;
    private Integer publicOf;
}
