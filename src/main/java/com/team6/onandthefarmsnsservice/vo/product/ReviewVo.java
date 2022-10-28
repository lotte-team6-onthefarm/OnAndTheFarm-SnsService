package com.team6.onandthefarmsnsservice.vo.product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewVo {

    private Long reviewId;

    private Integer reviewRate;
}
