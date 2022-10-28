package com.team6.onandthefarmsnsservice.dto.profile;

import lombok.Data;

@Data
public class MemberProfileDto {

    private Long memberId;

    private String memberRole;

    private String userName;

    private String memberProfileImage;

    private Long loginMemberId;

    private String loginMemberRole;
}