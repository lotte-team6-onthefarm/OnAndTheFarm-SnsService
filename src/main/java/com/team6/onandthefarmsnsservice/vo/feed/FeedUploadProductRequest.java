package com.team6.onandthefarmsnsservice.vo.feed;

import java.util.List;

import com.team6.onandthefarmsnsservice.vo.feed.imageProduct.ImageProductInfo;

import lombok.Data;

@Data
public class FeedUploadProductRequest {

    private List<ImageProductInfo> feedProductIdList;
}
