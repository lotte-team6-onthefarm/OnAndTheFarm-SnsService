package com.team6.onandthefarmsnsservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team6.onandthefarmsnsservice.entity.FeedImage;
import com.team6.onandthefarmsnsservice.entity.FeedImageProduct;

public interface FeedImageProductRepository extends JpaRepository<FeedImageProduct, Long> {

    List<FeedImageProduct> findByFeedImage(FeedImage feedImage);
}
