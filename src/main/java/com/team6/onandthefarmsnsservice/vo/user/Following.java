package com.team6.onandthefarmsnsservice.vo.user;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Following {
    private Long followingId;

    private Long followingMemberId;

    private String followingMemberRole;

    private Long followerMemberId;

    private String followerMemberRole;
}
