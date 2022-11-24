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
        name="FEED_IMAGE_SEQ_GENERATOR",
        sequenceName = "FEED_IMAGE_SEQ",
        initialValue = 100000, allocationSize = 1
)
public class FeedImage {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "FEED_IMAGE_SEQ_GENERATOR")
    private Long feedImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedId")
    private Feed feed;

    @Column(length=1000)
    private String feedOriginImageSrc;

    @Column(length=1000)
    private String feedImageSrc;
}
