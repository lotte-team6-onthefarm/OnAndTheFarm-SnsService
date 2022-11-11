package com.team6.onandthefarmsnsservice.entity;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Builder
@Slf4j
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SequenceGenerator(
        name="FEED_IMAGE_PRODUCT_SEQ_GENERATOR",
        sequenceName = "FEED_IMAGE_PRODUCT_SEQ",
        initialValue = 100000, allocationSize = 1
)
public class FeedImageProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "FEED_IMAGE_PRODUCT_SEQ_GENERATOR")
    private Long feedImageProductId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedImageId")
    private FeedImage feedImage;

    private Long productId;

    private Integer positionX;

    private Integer positionY;
}
