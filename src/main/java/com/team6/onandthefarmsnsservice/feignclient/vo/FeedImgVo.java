package com.team6.onandthefarmsnsservice.feignclient.vo;

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
public class FeedImgVo {
	private Long feedImageId;
	private String feedImageSrc;
}
