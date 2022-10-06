package com.team6.onandthefarmsnsservice.service;

import com.team6.onandthefarmsnsservice.dto.FeedDto;
import com.team6.onandthefarmsnsservice.vo.FeedResponse;

import java.util.List;

public interface FeedService {
    List<FeedResponse> findByRecentFeedList(FeedDto feedDto);
}
