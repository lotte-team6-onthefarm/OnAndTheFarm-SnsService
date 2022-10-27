package com.team6.onandthefarmsnsservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.team6.onandthefarmsnsservice.entity.FeedComment;

public interface FeedCommentRepository extends CrudRepository<FeedComment,Long> {

    @Query("select c from FeedComment c where c.feed.feedId=:feedId and c.feedCommentStatus=true ORDER BY c.feedCommentCreateAt DESC")
    List<FeedComment> findByFeedId(@Param("feedId") Long feedId);
}
