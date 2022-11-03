package com.team6.onandthefarmsnsservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.team6.onandthefarmsnsservice.entity.Feed;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
    List<Feed> findByMemberId(Long memberId);

    @Query("select f from Feed f where f.memberId =:memberId and f.feedStatus=true")
    List<Feed> findFeedListByMemberId(@Param("memberId") Long memberId);

    @Query("select f from Feed f where f.memberId =:memberId and f.feedStatus=true")
    Page<Feed> findMainFeedListByMemberId(PageRequest pageRequest, @Param("memberId") Long memberId);

    @Query("select f from Feed f where f.feedId=:feedId and f.feedStatus=true")
    Optional<Feed> findByIdAndStatus(Long feedId);

    Optional<Feed> findFeedByFeedNumber(Long feedNumber);
}
