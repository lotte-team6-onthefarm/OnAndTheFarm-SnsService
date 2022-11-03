package com.team6.onandthefarmsnsservice.feignclient.vo;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedVo {

    private Long feedNumber;

    private Long memberId;
}