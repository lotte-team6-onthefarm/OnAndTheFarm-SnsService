package com.team6.onandthefarmsnsservice.vo.profile;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberProfileCountResponse {
    private Integer photoCount;
    private Integer scrapCount;
    private Integer wishCount;
    private Integer productCount;
}
