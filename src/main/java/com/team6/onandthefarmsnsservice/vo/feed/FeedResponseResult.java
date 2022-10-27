package com.team6.onandthefarmsnsservice.vo.feed;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedResponseResult {
    private List<FeedResponse> feedResponseList;

    private Integer currentPageNum;

    private Integer totalPageNum;

    private Integer totalElementNum;
}
