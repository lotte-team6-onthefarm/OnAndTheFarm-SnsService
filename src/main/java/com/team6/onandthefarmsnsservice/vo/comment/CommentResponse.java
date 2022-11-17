package com.team6.onandthefarmsnsservice.vo.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {

    private List<CommentDetailResponse> commentList;
    private Integer commentCount;
}
