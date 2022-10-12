package com.team6.onandthefarmsnsservice.vo.profile;

import com.team6.onandthefarmsnsservice.entity.FeedImage;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileMainScrapResponse {
	private Long feedId;
	private FeedImage feedImg;
	private String memberName;
}
