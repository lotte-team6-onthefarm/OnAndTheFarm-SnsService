package com.team6.onandthefarmsnsservice.vo.imageProduct;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageProductInfo {

    private Integer imageIndex;
    private Long productId;
    private Long feedImageId;
}
