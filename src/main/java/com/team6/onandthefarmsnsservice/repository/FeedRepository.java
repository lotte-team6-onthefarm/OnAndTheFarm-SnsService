package com.team6.onandthefarmsnsservice.repository;

import com.team6.onandthefarmsnsservice.entity.Feed;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FeedRepository extends CrudRepository<Feed,Long> {

}
