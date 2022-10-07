package com.team6.onandthefarmsnsservice.vo.profile.product;

import lombok.Data;

@Data
public class Product {
	private Long productId;

	private Long categoryId;

	private Long sellerId;

	private String productName;

	private Integer productPrice;

	private Integer productTotalStock;

	private String productMainImgSrc;

	private String productDetail;

	private String productDetailShort;

	private String productOriginPlace;

	private String productDeliveryCompany;

	private String productRegisterDate;

	private String productUpdateDate;

	private String productStatus;

	private Integer productWishCount;

	private Integer productSoldCount;

}
