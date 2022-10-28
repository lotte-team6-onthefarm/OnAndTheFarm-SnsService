package com.team6.onandthefarmsnsservice.vo.product;

import com.team6.onandthefarmsnsservice.vo.user.UserVo;

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
public class WishVo {
	private Long wishId;

	private Long productId;

	private Long userId;

	private Boolean wishStatus;
}
