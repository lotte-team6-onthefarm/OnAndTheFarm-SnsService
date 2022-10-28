package com.team6.onandthefarmsnsservice.vo.product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddableProductResponse {

    private Long productId;
    private String productName;
    private String productMainImgSrc;
    private Integer productPrice;
    private String sellerShopName;

}
