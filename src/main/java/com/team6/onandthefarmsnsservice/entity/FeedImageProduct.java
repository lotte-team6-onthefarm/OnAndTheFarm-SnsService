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
public class FeedImageProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long feedImageProductId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedImageId")
    private FeedImage feedImage;

    private Long productId;
}
