package com.team6.onandthefarmsnsservice.vo.profile.product;

import lombok.Data;

@Data
public class WishProductListResponse {
	private Long productId;

	private Long sellerName;

	private String productName;

	private Integer productPrice;

	private String productMainImgSrc;

	private String productOriginPlace;

	private String productStatus;

	private Integer productWishCount;

	private Integer productReviewCount;
}
