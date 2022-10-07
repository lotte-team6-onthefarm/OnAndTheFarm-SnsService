package com.team6.onandthefarmsnsservice.dto.comment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentInfoDto {

    private Long memberId;
    private String memberRole;
    private Long feedId;
    private Long feedCommentId;
    private String feedCommentContent;
}
