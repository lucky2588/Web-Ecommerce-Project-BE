package com.total.webecommerce.resquest.OfBlog;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentBlogResquest {
    @NotNull(message = "Không được để trống")
    private String email;
    @NotNull(message = "Không được để trống")
    private String commentOfUser;
}
