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
        name="FEED_TAG_SEQ_GENERATOR",
        sequenceName = "FEED_TAG_SEQ",
        initialValue = 100000, allocationSize = 1
)
public class FeedTag {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "FEED_TAG_SEQ_GENERATOR")
    private Long feedTagId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedId")
    private Feed feed;

    private String feedTagName;
}
