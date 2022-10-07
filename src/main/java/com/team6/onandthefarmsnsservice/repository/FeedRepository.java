package com.team6.onandthefarmsnsservice.repository;

import com.team6.onandthefarmsnsservice.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
    Feed findByMemberId(Long memberId);

    @Query("select f from Feed f where f.memberId =:memberId")
    List<Feed> findFeedListByMemberId(@Param("memberId") Long memberId);

    @Query("select f from Feed f where f.feedId=:feedId and f.feedStatus=:true")
    Optional<Feed> findByIdAndStatus(Long feedId);
}
