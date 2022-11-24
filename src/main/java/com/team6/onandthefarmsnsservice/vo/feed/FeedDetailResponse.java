package com.team6.onandthefarmsnsservice.vo.feed;

import java.util.List;

import com.team6.onandthefarmsnsservice.entity.FeedTag;
import com.team6.onandthefarmsnsservice.vo.feed.imageProduct.ImageInfo;
import com.team6.onandthefarmsnsservice.vo.feed.imageProduct.ImageProductResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedDetailResponse {

    private Long memberId;

    private String memberRole;

    private String memberName;

    private String memberProfileImg;

    private Long feedId;

    private String feedTitle;

    private String feedContent;

    private Integer feedViewCount;

    private Integer feedLikeCount;

    private Integer feedShareCount;

    private Integer feedScrapCount;

    private Integer feedCommentCount;

    private String feedCreateAt;

    private String feedUpdateAt;

    private List<ImageInfo> feedOriginImageList;

    private List<ImageInfo> feedImageList;

    private List<ImageProductResponse> feedImageProductList;

    private List<FeedTag> feedTag;

    private Boolean feedLikeStatus;

    private Boolean scrapStatus;

    private Boolean isModifiable;

    private Boolean followStatus;

    private Long feedNumber;

}
