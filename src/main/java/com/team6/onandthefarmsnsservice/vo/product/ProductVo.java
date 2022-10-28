package com.team6.onandthefarmsnsservice.vo.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductVo {

	private Long productId;

	private Long sellerId;

	private String productName;

	private Integer productPrice;

	private String productMainImgSrc;

}
