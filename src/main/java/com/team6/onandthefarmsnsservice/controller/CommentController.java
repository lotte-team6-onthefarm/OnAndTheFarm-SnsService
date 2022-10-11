package com.team6.onandthefarmsnsservice.controller;

import com.team6.onandthefarmsnsservice.dto.comment.CommentInfoDto;
import com.team6.onandthefarmsnsservice.service.CommentService;
import com.team6.onandthefarmsnsservice.utils.BaseResponse;
import com.team6.onandthefarmsnsservice.vo.comment.CommentDetailResponse;
import com.team6.onandthefarmsnsservice.vo.comment.CommentRequest;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/user/sns/comment")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @PostMapping
    @ApiOperation("피드 댓글 등록")
    public ResponseEntity<BaseResponse> addComment(@ApiIgnore Principal principal, @RequestBody CommentRequest commentUploadRequest){

        Long memberId = null;
        String memberRole = null;
        CommentInfoDto commentInfoDto = CommentInfoDto.builder()
                .memberId(memberId)
                .memberRole(memberRole)
                .feedId(commentUploadRequest.getFeedId())
                .feedCommentContent(commentUploadRequest.getFeedCommentContent()).build();

        Long commentId = commentService.addComment(commentInfoDto);

        BaseResponse baseResponse = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("add comment success")
                .data(commentId)
                .build();
        if(commentId == null){
            baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("add comment fail")
                    .build();
            return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(baseResponse, HttpStatus.OK);
    }

    @PutMapping
    @ApiOperation("피드 댓글 수정")
    public ResponseEntity<BaseResponse> modifyComment(@ApiIgnore Principal principal, @RequestBody CommentRequest commentModityRequest){

        Long memberId = null;
        String memberRole = null;
        CommentInfoDto commentInfoDto = CommentInfoDto.builder()
                .memberId(memberId)
                .memberRole(memberRole)
                .feedCommentId(commentModityRequest.getFeedCommentId())
                .feedCommentContent(commentModityRequest.getFeedCommentContent()).build();

        Long commentId = commentService.modifyComment(commentInfoDto);

        BaseResponse baseResponse = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("add comment success")
                .data(commentId)
                .build();
        if(commentId == null){
            baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("add comment fail")
                    .build();
            return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(baseResponse, HttpStatus.OK);
    }

    @GetMapping
    @ApiOperation("피드 댓글 조회")
    public ResponseEntity<BaseResponse<List<CommentDetailResponse>>> findComment(@ApiIgnore Principal principal, @RequestParam Long feedId){

        Long memberId = Long.parseLong(principal.getName());
        List<CommentDetailResponse> commentList = commentService.findCommentDetail(feedId, memberId);

        BaseResponse baseResponse = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("find comment success")
                .data(commentList)
                .build();
        if(commentList == null){
            baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("find comment fail")
                    .build();
            return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(baseResponse, HttpStatus.OK);
    }
}
