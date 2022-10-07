package com.team6.onandthefarmsnsservice.vo.profile;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileMainWishResponse {
	private Long productId;
	private String productImgSrc;
}
