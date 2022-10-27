package com.team6.onandthefarmsnsservice.vo.profile;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WishProductListResponse {
	private Long productId;

	private String productName;

	private Integer productPrice;

	private String productMainImgSrc;

	private String productOriginPlace;

	private String productStatus;

	private Integer productWishCount;

	private String sellerName;

	private Integer productReviewCount;

	private Long reviewRate;

	private Integer reviewCount;
}
