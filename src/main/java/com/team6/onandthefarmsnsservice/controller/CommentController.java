package com.team6.onandthefarmsnsservice.controller;

import com.team6.onandthefarmsnsservice.dto.comment.CommentInfoDto;
import com.team6.onandthefarmsnsservice.service.CommentService;
import com.team6.onandthefarmsnsservice.utils.BaseResponse;
import com.team6.onandthefarmsnsservice.vo.comment.CommentDetailResponse;
import com.team6.onandthefarmsnsservice.vo.comment.CommentRequest;
import com.team6.onandthefarmsnsservice.vo.comment.CommentResponse;
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

        String[] principalInfo = principal.getName().split(" ");
        Long memberId = Long.parseLong(principalInfo[0]);
        String memberRole = principalInfo[1];

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

    @PutMapping("/modify")
    @ApiOperation("피드 댓글 수정")
    public ResponseEntity<BaseResponse> modifyComment(@ApiIgnore Principal principal, @RequestBody CommentRequest commentModifyRequest){

        String[] principalInfo = principal.getName().split(" ");
        Long memberId = Long.parseLong(principalInfo[0]);
        String memberRole = principalInfo[1];

        CommentInfoDto commentInfoDto = CommentInfoDto.builder()
                .memberId(memberId)
                .memberRole(memberRole)
                .feedCommentId(commentModifyRequest.getFeedCommentId())
                .feedCommentContent(commentModifyRequest.getFeedCommentContent()).build();

        Long commentId = commentService.modifyComment(commentInfoDto);

        BaseResponse baseResponse = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("modify comment success")
                .data(commentId)
                .build();
        if(commentId == null){
            baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("modify comment fail")
                    .build();
            return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(baseResponse, HttpStatus.OK);
    }

    @PutMapping("/delete")
    @ApiOperation("피드 댓글 삭제")
    public ResponseEntity<BaseResponse> deleteComment(@ApiIgnore Principal principal, @RequestBody CommentRequest commentDeleteRequest){

        String[] principalInfo = principal.getName().split(" ");
        Long memberId = Long.parseLong(principalInfo[0]);
        String memberRole = principalInfo[1];

        CommentInfoDto commentInfoDto = CommentInfoDto.builder()
                .memberId(memberId)
                .memberRole(memberRole)
                .feedCommentId(commentDeleteRequest.getFeedCommentId())
                .build();


        Long commentId = commentService.deleteComment(commentInfoDto);

        BaseResponse baseResponse = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("delete comment success")
                .data(commentId)
                .build();
        if(commentId == null){
            baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("delete comment fail")
                    .build();
            return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(baseResponse, HttpStatus.OK);
    }

    @GetMapping("/list")
    @ApiOperation("피드 댓글 조회")
    public ResponseEntity<BaseResponse<CommentResponse>> findComment(@ApiIgnore Principal principal, @RequestParam Long feedId){

        Long memberId = 0L;

        if(principal != null){
            String[] principalInfo = principal.getName().split(" ");
            memberId = Long.parseLong(principalInfo[0]);
        }

        CommentResponse commentResponse = commentService.findCommentDetail(feedId, memberId);

        BaseResponse baseResponse = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("find comment success")
                .data(commentResponse)
                .build();
        if(commentResponse == null){
            baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("find comment fail")
                    .build();
            return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(baseResponse, HttpStatus.OK);
    }
}
