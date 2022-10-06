package com.team6.onandthefarmsnsservice.service;


import com.team6.onandthefarmsnsservice.dto.FeedDto;
import com.team6.onandthefarmsnsservice.vo.FeedResponse;
import com.team6.onandthefarmsnsservice.dto.FeedInfoDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FeedService {

    List<FeedResponse> findByRecentFeedList(FeedDto feedDto);
    
    Long uploadFeed(Long memberId, String memberRole, FeedInfoDto feedInfoDto);
    
}
