package com.team6.onandthefarmsnsservice.vo;

import lombok.*;

import java.util.List;

@Data
public class FeedUploadProductRequest {

    private List<ImageProduct> feedProductIdList;
}
