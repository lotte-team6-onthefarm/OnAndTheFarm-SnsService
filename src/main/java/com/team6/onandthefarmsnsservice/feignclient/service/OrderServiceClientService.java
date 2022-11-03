package com.team6.onandthefarmsnsservice.feignclient.service;

import com.team6.onandthefarmsnsservice.feignclient.vo.FeedVo;

public interface OrderServiceClientService {

    FeedVo findFeedByFeedNumber(Long feedNumber);
}
