package com.total.webecommerce.resquest.OfProduct;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentProductResquest {
    private String email;
    @NotNull(message = "Không được để trống bình luận  ")
    private String contentComment;
}
