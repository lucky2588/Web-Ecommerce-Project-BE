package com.total.webecommerce.resquest.OfBlog;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "Không được để trống")
    private String title;
    @NotNull(message = "Không được để trống")
    private String content;
    @NotNull(message = "Không được để trống")
    private String description;
    @NotNull(message = "Không được để trống")
    private String detail;
    @NotNull(message = "Không được để trống")
    private Double price;
    @NotNull(message = "Không được để trống")
    private Integer nums;
    private Integer brandId;
    private Integer publicOf;
}
