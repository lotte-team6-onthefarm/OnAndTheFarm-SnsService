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
        name="FEED_LIKE_SEQ_GENERATOR",
        sequenceName = "FEED_LIKE_SEQ",
        initialValue = 100000, allocationSize = 1
)
public class FeedLike {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "FEED_LIKE_SEQ_GENERATOR")
    private Long feedLikeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedId")
    private Feed feed;

    private Long memberId;
}
