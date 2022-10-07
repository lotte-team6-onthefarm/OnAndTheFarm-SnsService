package com.team6.onandthefarmsnsservice.repository;

import com.team6.onandthefarmsnsservice.entity.Feed;
import com.team6.onandthefarmsnsservice.entity.FeedTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedTagRepository extends JpaRepository<FeedTag, Long> {

    List<FeedTag> findByFeed(Feed feed);
}
