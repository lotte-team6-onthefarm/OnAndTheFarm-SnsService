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
        name="FEED_SEQ_GENERATOR",
        sequenceName = "FEED_SEQ",
        initialValue = 100000, allocationSize = 1
)
public class Feed {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "FEED_SEQ_GENERATOR")
    private Long feedId;

    private Long memberId;

    private String memberRole;

    private String feedTitle;

    @Column(length=1000)
    private String feedContent;

    private Integer feedViewCount;

    private Integer feedLikeCount;

    private String feedCreateAt;

    private String feedUpdateAt;

    private Integer feedShareCount;

    private Boolean feedStatus; // true : 피드게시중 / false : 피드 삭제

    private Integer feedScrapCount;

    private Integer feedCommentCount;

    private Long feedNumber;

}
