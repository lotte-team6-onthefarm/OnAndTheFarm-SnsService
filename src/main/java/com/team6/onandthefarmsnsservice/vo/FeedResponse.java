package com.team6.onandthefarmsnsservice.vo;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedResponse {
    private Long feedId;

    private Long memberId;

    private Integer memberRoll;

    private String memberName;

    private String feedImageSrc;

    private Integer feedViewCount;

    private String feedTitle;

    private Integer feedLikeCount;

    private Integer feedShareCount;

    private Integer feedScrapCount;

    private Integer feedCommentCount;

}
