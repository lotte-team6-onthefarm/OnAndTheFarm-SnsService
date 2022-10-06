package com.team6.onandthefarmsnsservice.controller;

import com.team6.onandthefarmsnsservice.dto.SnsFeedDto;
import com.team6.onandthefarmsnsservice.service.FeedService;
import com.team6.onandthefarmsnsservice.utils.BaseResponse;
import com.team6.onandthefarmsnsservice.vo.FeedUploadRequest;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class SnsController {

    private final FeedService feedService;

    @Autowired
    public SnsController(FeedService feedService){
        this.feedService = feedService;
    }

    @PostMapping("/sns/upload")
    @ApiOperation("sns 피드 업로드")
    public ResponseEntity<BaseResponse> uploadFeed(@RequestBody FeedUploadRequest feedUploadRequest){

        SnsFeedDto snsFeedDto = null;

        Long feedId = feedService.uploadFeed(snsFeedDto);


        BaseResponse baseResponse = BaseResponse.builder()
                .httpStatus(HttpStatus.CREATED)
                .message("feed upload success")
                .data(feedId)
                .build();
        if(feedId == null){
            baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .message("feed upload fail")
                    .build();
        }

        return new ResponseEntity(baseResponse, HttpStatus.CREATED);
    }
}
