package com.team6.onandthefarmsnsservice.service;

import com.team6.onandthefarmsnsservice.dto.SnsFeedDto;
import com.team6.onandthefarmsnsservice.repository.FeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedServiceImpl implements FeedService{

    private final FeedRepository feedRepository;

    @Autowired
    public FeedServiceImpl(FeedRepository feedRepository){
        this.feedRepository = feedRepository;
    }


    @Override
    public Long uploadFeed(SnsFeedDto snsFeedDto) {
        return null;
    }
}
