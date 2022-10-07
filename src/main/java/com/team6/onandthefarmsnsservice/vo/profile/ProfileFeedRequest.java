package com.team6.onandthefarmsnsservice.vo.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileFeedRequest {
    private Long memberId;
    private Integer pageNumber;
}
