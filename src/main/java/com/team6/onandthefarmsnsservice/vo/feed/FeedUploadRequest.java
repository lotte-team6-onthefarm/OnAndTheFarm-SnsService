package com.team6.onandthefarmsnsservice.vo.feed;

import lombok.*;

import java.util.List;

@Data
public class FeedUploadRequest {

    private String feedTitle;
    private String feedContent;
    private List<String> feedTag;

}
