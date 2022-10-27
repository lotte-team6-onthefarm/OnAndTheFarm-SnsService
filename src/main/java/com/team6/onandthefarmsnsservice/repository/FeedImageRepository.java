package com.team6.onandthefarmsnsservice.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.team6.onandthefarmsnsservice.entity.Feed;
import com.team6.onandthefarmsnsservice.entity.FeedImage;

public interface FeedImageRepository extends CrudRepository<FeedImage,Long> {
    List<FeedImage> findByFeed(Feed feed);
}
