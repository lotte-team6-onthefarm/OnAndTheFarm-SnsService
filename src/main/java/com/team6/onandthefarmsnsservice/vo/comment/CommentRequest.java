package com.team6.onandthefarmsnsservice.vo.comment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentRequest {

    private Long feedId;
    private Long feedCommentId;
    private String feedCommentContent;

}
