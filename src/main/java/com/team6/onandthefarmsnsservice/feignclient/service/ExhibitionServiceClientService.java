package com.team6.onandthefarmsnsservice.feignclient.service;

import com.team6.onandthefarmsnsservice.feignclient.vo.FeedImgVo;
import com.team6.onandthefarmsnsservice.feignclient.vo.FeedInfoVo;

public interface ExhibitionServiceClientService {

	FeedInfoVo getFeedInfoVoByFeedId(Long feedId);

	FeedImgVo getFeedImgVoByFeedId(Long feedId);
}
