package com.team6.onandthefarmsnsservice.vo.profile;

import java.awt.*;

import com.team6.onandthefarmsnsservice.entity.FeedImage;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileMainFeedResponse {
	private Long feedId;
	private FeedImage feedImg;
}
