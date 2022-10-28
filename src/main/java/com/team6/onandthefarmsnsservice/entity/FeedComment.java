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
        name="FEED_COMMENT_SEQ_GENERATOR",
        sequenceName = "FEED_COMMENT_SEQ",
        initialValue = 100000, allocationSize = 1
)
public class FeedComment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "FEED_COMMENT_SEQ_GENERATOR")
    private Long feedCommnetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedId")
    private Feed feed;

    private Long memberId;

    private String memberRole;

    private String feedCommentContent;

    private String feedCommentCreateAt;

    private String feedCommentModifiedAt;

    private Boolean feedCommentStatus;
}
