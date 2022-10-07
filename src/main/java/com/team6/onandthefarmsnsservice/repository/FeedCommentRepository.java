package com.team6.onandthefarmsnsservice.repository;

import com.team6.onandthefarmsnsservice.entity.FeedComment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedCommentRepository extends CrudRepository<FeedComment,Long> {

    @Query("select c from FeedComment c where c.feed.feedId=:feedId")
    List<FeedComment> findByFeedId(@Param("feedId") Long feedId);
}
