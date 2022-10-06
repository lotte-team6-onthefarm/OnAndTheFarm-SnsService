package com.team6.onandthefarmsnsservice.vo;

import com.team6.onandthefarmsnsservice.vo.imageProduct.ImageProductInfo;
import lombok.*;

import java.util.List;

@Data
public class FeedUploadProductRequest {

    private List<ImageProductInfo> feedProductIdList;
}
