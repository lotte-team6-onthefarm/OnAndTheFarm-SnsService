package com.team6.onandthefarmsnsservice.repository;

import com.team6.onandthefarmsnsservice.entity.FeedImage;
import com.team6.onandthefarmsnsservice.entity.FeedImageProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FeedImageProductRepository extends JpaRepository<FeedImageProduct, Long> {

    List<FeedImageProduct> findByFeedImage(FeedImage feedImage);
}
