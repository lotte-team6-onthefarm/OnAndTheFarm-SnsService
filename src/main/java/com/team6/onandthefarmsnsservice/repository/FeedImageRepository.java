package com.team6.onandthefarmsnsservice.repository;

import com.team6.onandthefarmsnsservice.entity.Feed;
import com.team6.onandthefarmsnsservice.entity.FeedImage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedImageRepository extends CrudRepository<FeedImage,Long> {
    List<FeedImage> findByFeed(Feed feed);
}
