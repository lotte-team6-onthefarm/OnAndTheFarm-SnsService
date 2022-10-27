package com.team6.onandthefarmsnsservice.vo.feed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class FeedResponse {
    private Long feedId;

    private Long memberId;

    private String memberRole;

    private String memberName;

    private String memberProfileImg;

    private String feedImageSrc;

    private Integer feedViewCount;

    private String feedTitle;

    private Integer feedLikeCount;

    private Integer feedShareCount;

    private Integer feedScrapCount;

    private Integer feedCommentCount;

    private String feedContent;

    private Boolean feedLikeStatus;

    private Boolean scrapStatus;

    private Boolean followStatus;

    private Boolean isModifiable;

}
