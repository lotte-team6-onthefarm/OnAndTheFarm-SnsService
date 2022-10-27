package com.team6.onandthefarmsnsservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team6.onandthefarmsnsservice.entity.Feed;
import com.team6.onandthefarmsnsservice.entity.FeedTag;

public interface FeedTagRepository extends JpaRepository<FeedTag, Long> {

    List<FeedTag> findByFeed(Feed feed);

    List<FeedTag> findByFeedTagName(String feedTagName);
}
