package com.team6.onandthefarmsnsservice.feignclient.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team6.onandthefarmsnsservice.entity.Feed;
import com.team6.onandthefarmsnsservice.entity.FeedImage;
import com.team6.onandthefarmsnsservice.feignclient.vo.FeedImgVo;
import com.team6.onandthefarmsnsservice.feignclient.vo.FeedInfoVo;
import com.team6.onandthefarmsnsservice.repository.FeedImageRepository;
import com.team6.onandthefarmsnsservice.repository.FeedRepository;

@Service
@Transactional
public class ExhibitionServiceClientServiceImpl implements ExhibitionServiceClientService {
	private final FeedRepository feedRepository;

	private final FeedImageRepository feedImageRepository;

	public ExhibitionServiceClientServiceImpl(FeedRepository feedRepository,
			FeedImageRepository feedImageRepository) {
		this.feedRepository = feedRepository;
		this.feedImageRepository = feedImageRepository;
	}

	@Override
	public FeedInfoVo getFeedInfoVoByFeedId(Long feedId){
		Feed feed = feedRepository.findById(feedId).get();
		FeedInfoVo feedInfoVo = FeedInfoVo.builder()
				.feedId(feed.getFeedId())
				.memberId(feed.getMemberId())
				.memberRole(feed.getMemberRole())
				.feedTitle(feed.getFeedTitle())
				.feedContent(feed.getFeedContent())
				.feedViewCount(feed.getFeedViewCount())
				.feedLikeCount(feed.getFeedLikeCount())
				.feedCreateAt(feed.getFeedCreateAt())
				.feedUpdateAt(feed.getFeedUpdateAt())
				.feedShareCount(feed.getFeedShareCount())
				.feedStatus(feed.getFeedStatus())
				.feedScrapCount(feed.getFeedScrapCount())
				.feedCommentCount(feed.getFeedCommentCount())
				.feedNumber(feed.getFeedNumber())
				.build();
		return feedInfoVo;
	}
	@Override
	public FeedImgVo getFeedImgVoByFeedId(Long feedId){
		Feed feed = feedRepository.findById(feedId).get();
		FeedImage feedImage = feedImageRepository.findByFeed(feed).get(0);

		FeedImgVo feedImgVo = FeedImgVo.builder()
				.feedImageId(feedImage.getFeedImageId())
				.feedImageSrc(feedImage.getFeedImageSrc())
				.build();

		return feedImgVo;
	}
}
