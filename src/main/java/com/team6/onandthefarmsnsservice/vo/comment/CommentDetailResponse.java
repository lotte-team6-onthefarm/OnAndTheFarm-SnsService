package com.team6.onandthefarmsnsservice.vo.comment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentDetailResponse {

    private Long memberId;

    private String memberRole;
    
    private String memberName;

    private Long feedCommnetId;

    private String feedCommentContent;

    private String feedCommentCreateAt;

    private String feedCommentModifiedAt;

    private Boolean isModifiable;

    public CommentDetailResponse(){
        this.isModifiable = false;
    }
}
