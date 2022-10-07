package com.team6.onandthefarmsnsservice.service;


import com.team6.onandthefarmsnsservice.dto.FeedDto;
import com.team6.onandthefarmsnsservice.dto.profile.ProfileMainFeedDto;
import com.team6.onandthefarmsnsservice.dto.profile.ProfileMainScrapDto;
import com.team6.onandthefarmsnsservice.dto.profile.ProfileMainWishDto;
import com.team6.onandthefarmsnsservice.vo.FeedDetailResponse;
import com.team6.onandthefarmsnsservice.vo.FeedResponse;
import com.team6.onandthefarmsnsservice.dto.FeedInfoDto;
import com.team6.onandthefarmsnsservice.vo.profile.ProfileMainFeedResponse;
import com.team6.onandthefarmsnsservice.vo.profile.ProfileMainScrapResponse;
import com.team6.onandthefarmsnsservice.vo.profile.ProfileMainWishResponse;

import java.util.List;

public interface FeedService {

    List<FeedResponse> findByRecentFeedList(FeedDto feedDto);

    List<FeedResponse> findByLikeFeedList(Integer pageNumber);

    List<FeedResponse> findByFollowFeedList(Long memberId,Integer pageNumber);

    List<FeedResponse> findByViewCountFeedList(Integer pageNumber);

    Boolean createFeedLike(Long feedId, Long userId);

    Boolean createFeedScrap(Long feedId, Long userId);

    Long uploadFeed(Long memberId, String memberRole, FeedInfoDto feedInfoDto);

    FeedDetailResponse findFeedDetail(Long feedId);

    Boolean upViewCount(Long feedId);

    List<ProfileMainFeedResponse> findByMemberFeedList(ProfileMainFeedDto profileMainFeedDto);

    List<ProfileMainScrapResponse> findByMemberScrapList(ProfileMainScrapDto profileMainScrapDto);

    List<ProfileMainWishResponse> findByMemberWishList(ProfileMainWishDto profileMainWishDto);
    
}
