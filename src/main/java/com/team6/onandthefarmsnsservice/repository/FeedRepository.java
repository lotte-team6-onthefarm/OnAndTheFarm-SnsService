package com.team6.onandthefarmsnsservice.repository;

import com.team6.onandthefarmsnsservice.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FeedRepository extends JpaRepository<Feed,Long> {
    Feed findByMemberId(Long memberId);
}
