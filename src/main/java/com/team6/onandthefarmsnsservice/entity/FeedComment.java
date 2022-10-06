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
public class FeedComment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long feedCommnetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedId")
    private Feed feed;

    private String feedCommentContent;

    private String feedCommentCreateAt;

    private String feedCommentModifiedAt;
}
