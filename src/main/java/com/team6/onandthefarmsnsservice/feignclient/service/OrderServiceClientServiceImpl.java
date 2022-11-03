package com.team6.onandthefarmsnsservice.feignclient.service;

import com.fasterxml.jackson.annotation.OptBoolean;
import com.team6.onandthefarmsnsservice.entity.Feed;
import com.team6.onandthefarmsnsservice.feignclient.vo.FeedVo;
import com.team6.onandthefarmsnsservice.repository.FeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class OrderServiceClientServiceImpl implements OrderServiceClientService{

    private final FeedRepository feedRepository;

    @Autowired
    public OrderServiceClientServiceImpl(FeedRepository feedRepository){
        this.feedRepository = feedRepository;
    }

    @Override
    public FeedVo findFeedByFeedNumber(Long feedNumber) {
        Optional<Feed> feed = feedRepository.findFeedByFeedNumber(feedNumber);

        FeedVo feedVo = FeedVo.builder()
                .feedNumber(feed.get().getFeedNumber())
                .memberId(feed.get().getMemberId())
                .build();

        return feedVo;
    }
}
