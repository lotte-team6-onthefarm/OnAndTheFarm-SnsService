package com.team6.onandthefarmsnsservice.vo.feed;

import com.team6.onandthefarmsnsservice.entity.FeedTag;
import com.team6.onandthefarmsnsservice.vo.feed.imageProduct.ImageInfo;
import com.team6.onandthefarmsnsservice.vo.feed.imageProduct.ImageProductInfo;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FeedDetailResponse {

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

    private List<ImageInfo> feedImageList;

    private List<ImageProductInfo> feedImageProductList;

    private List<FeedTag> feedTag;

}
