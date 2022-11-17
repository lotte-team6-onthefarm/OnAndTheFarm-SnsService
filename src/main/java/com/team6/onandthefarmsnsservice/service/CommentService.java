package com.team6.onandthefarmsnsservice.service;

import java.util.List;

import com.team6.onandthefarmsnsservice.dto.comment.CommentInfoDto;
import com.team6.onandthefarmsnsservice.vo.comment.CommentDetailResponse;
import com.team6.onandthefarmsnsservice.vo.comment.CommentResponse;

public interface CommentService {

    CommentResponse findCommentDetail(Long feedId, Long memberId);

    Long addComment(CommentInfoDto commentInfoDto);

    Long modifyComment(CommentInfoDto commentInfoDto);

    Long deleteComment(CommentInfoDto commentInfoDto);
}
