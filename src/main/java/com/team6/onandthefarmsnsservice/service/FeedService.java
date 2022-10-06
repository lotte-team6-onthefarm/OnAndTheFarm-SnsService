package com.team6.onandthefarmsnsservice.service;


import com.team6.onandthefarmsnsservice.dto.FeedDto;
import com.team6.onandthefarmsnsservice.vo.FeedResponse;
import com.team6.onandthefarmsnsservice.dto.SnsFeedDto;

import java.util.List;

public interface FeedService {

    List<FeedResponse> findByRecentFeedList(FeedDto feedDto);

    List<FeedResponse> findByLikeFeedList(Integer pageNumber);

    List<FeedResponse> findByFollowFeedList(Long memberId,Integer pageNumber);

    List<FeedResponse> findByViewCountFeedList(Integer pageNumber);
    
    Long uploadFeed(SnsFeedDto snsFeedDto);

    Boolean createFeedLike(Long feedId, Long userId);

    Boolean createFeedScrap(Long feedId, Long userId);
    
}
