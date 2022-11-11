package com.team6.onandthefarmsnsservice.vo.feed.imageProduct;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageProductResponse {

    private Long feedImageId;

    private Long productId;

    private String productName;

    private Integer productPrice;

    private String productMainImgSrc;

    private String sellerName;

    private Integer posX;

    private Integer posY;
}
