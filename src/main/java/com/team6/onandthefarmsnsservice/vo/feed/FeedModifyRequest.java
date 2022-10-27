package com.team6.onandthefarmsnsservice.vo.feed;

import java.util.List;

import lombok.Data;

@Data
public class FeedModifyRequest {

    private Long feedId;
    private String feedTitle;
    private String feedContent;
    private List<String> feedTag;
    private List<Long> deleteImg;
}
