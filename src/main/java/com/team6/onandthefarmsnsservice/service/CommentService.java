package com.team6.onandthefarmsnsservice.service;

import com.team6.onandthefarmsnsservice.dto.comment.CommentInfoDto;
import com.team6.onandthefarmsnsservice.vo.comment.CommentDetailResponse;

import java.util.List;

public interface CommentService {

    List<CommentDetailResponse> findCommentDetail(Long feedId, Long memberId);

    Long addComment(CommentInfoDto commentInfoDto);

    Long modifyComment(CommentInfoDto commentInfoDto);
}
