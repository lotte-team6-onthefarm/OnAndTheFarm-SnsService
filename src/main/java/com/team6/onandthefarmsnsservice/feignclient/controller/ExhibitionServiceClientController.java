package com.team6.onandthefarmsnsservice.feignclient.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.team6.onandthefarmsnsservice.feignclient.service.ExhibitionServiceClientService;
import com.team6.onandthefarmsnsservice.feignclient.vo.FeedImgVo;
import com.team6.onandthefarmsnsservice.feignclient.vo.FeedInfoVo;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class ExhibitionServiceClientController {
	private final ExhibitionServiceClientService exhibitionServiceClientService;

	public ExhibitionServiceClientController(
			ExhibitionServiceClientService exhibitionServiceClientService) {
		this.exhibitionServiceClientService = exhibitionServiceClientService;
	}

	@GetMapping("/api/feign/user/feed/feed-service/feed/{feed-no}")
	public FeedInfoVo findFeedInfoVoByFeedId(@PathVariable("feed-no") Long feedId){
		return exhibitionServiceClientService.getFeedInfoVoByFeedId(feedId);
	}

	@GetMapping("/api/feign/user/feed/feed-service/feedImgVo/{feed-no}")
	public FeedImgVo findFeedImgVoByFeedId(@PathVariable("feed-no") Long feedId){
		return exhibitionServiceClientService.getFeedImgVoByFeedId(feedId);
	}


}
