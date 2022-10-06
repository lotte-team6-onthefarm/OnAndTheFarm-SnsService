package com.team6.onandthefarmsnsservice.service;


import com.team6.onandthefarmsnsservice.dto.FeedDto;
import com.team6.onandthefarmsnsservice.vo.FeedResponse;
import com.team6.onandthefarmsnsservice.dto.SnsFeedDto;

import java.util.List;

public interface FeedService {

    List<FeedResponse> findByRecentFeedList(FeedDto feedDto);
    
    Long uploadFeed(SnsFeedDto snsFeedDto);
    
}
