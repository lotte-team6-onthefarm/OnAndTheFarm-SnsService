package com.team6.onandthefarmsnsservice.entity;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Builder
@Slf4j
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
/**
 * memberRoll => 1: user 2: seller
 */
public class Feed {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long feedId;

    private Long memberId;

    private String memberRole;

    private String feedTitle;

    private String feedContent;

    private Integer feedViewCount;

    private Integer feedLikeCount;

    private String feedCreateAt;

    private String feedUpdateAt;

    private Integer feedShareCount;

    private Boolean feedStatus; // true : 피드게시중 / false : 피드 삭제

    private Integer feedScrapCount;

    private Integer feedCommentCount;

}
