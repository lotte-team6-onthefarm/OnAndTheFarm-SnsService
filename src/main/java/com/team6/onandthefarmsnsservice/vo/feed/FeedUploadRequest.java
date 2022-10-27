package com.team6.onandthefarmsnsservice.vo.feed;

import java.util.List;

import lombok.Data;

@Data
public class FeedUploadRequest {

    private String feedTitle;
    private String feedContent;
    private List<String> feedTag;

}
