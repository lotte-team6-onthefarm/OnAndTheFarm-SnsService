package com.team6.onandthefarmsnsservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.team6.onandthefarmsnsservice.entity.Feed;
import com.team6.onandthefarmsnsservice.entity.FeedLike;

public interface FeedLikeRepository extends CrudRepository<FeedLike,Long> {
    Optional<FeedLike> findByFeedAndMemberId(Feed feed,Long memberId);

    @Query("select l from FeedLike l where l.memberId=:memberId")
    List<FeedLike> findFeedLikeListByMemberId(@Param("memberId") Long memberId);

}
