package com.team6.onandthefarmsnsservice.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.team6.onandthefarmsnsservice.vo.feed.imageProduct.ImageProductInfo;

import lombok.Data;

@Data
public class FeedInfoDto {

    private Long feedId;

    private String feedTitle;

    private String feedContent;

    private List<String> feedTag;

    private List<MultipartFile> feedImgSrcList;

    private List<ImageProductInfo> feedProductIdList;

    private List<MultipartFile> originFeedImages;

    private List<Long> deleteImg;

}
