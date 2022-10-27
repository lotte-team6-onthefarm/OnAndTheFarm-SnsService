package com.team6.onandthefarmsnsservice.vo.profile;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileMainScrapResponse {
	private Long feedId;
	private Long feedImageId;
	private String feedImageSrc;
	private String memberName;
}
