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
public class FeedLike {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long feedLikeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedId")
    private Feed feed;

    private Long memberId;
}
