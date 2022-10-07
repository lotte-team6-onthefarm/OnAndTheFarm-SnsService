package com.team6.onandthefarmsnsservice.vo.feed;

import com.team6.onandthefarmsnsservice.vo.feed.imageProduct.ImageProductInfo;
import lombok.*;

import java.util.List;

@Data
public class FeedUploadProductRequest {

    private List<ImageProductInfo> feedProductIdList;
}
