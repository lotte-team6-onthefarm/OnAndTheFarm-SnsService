package com.team6.onandthefarmsnsservice.controller;

import com.team6.onandthefarmsnsservice.dto.FeedDto;
import com.team6.onandthefarmsnsservice.service.FeedService;
import com.team6.onandthefarmsnsservice.utils.BaseResponse;
import com.team6.onandthefarmsnsservice.vo.FeedRequest;
import com.team6.onandthefarmsnsservice.vo.FeedResponse;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user/feed")
public class FeedUserController {

    private FeedService feedService;

    @Autowired
    public FeedUserController(FeedService feedService) {
        this.feedService = feedService;
    }

    @GetMapping("list")
    @ApiOperation(value = "메인 피드 최근 조회")
    public ResponseEntity<BaseResponse<List<FeedResponse>>> findByRecentFeedList(@RequestParam FeedRequest feedRequest){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        FeedDto FeedDto = modelMapper.map(feedRequest, FeedDto.class);

        List<FeedResponse> responses = feedService.findByRecentFeedList(FeedDto);

        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(responses)
                .build();

        return new ResponseEntity(response,HttpStatus.OK);
    }
}
