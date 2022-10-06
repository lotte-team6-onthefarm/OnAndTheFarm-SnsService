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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user/feed")
public class FeedKshController {

    private FeedService feedService;

    @Autowired
    public FeedKshController(FeedService feedService) {
        this.feedService = feedService;
    }

    @GetMapping("/list")
    @ApiOperation(value = "메인 피드 최신순 조회")
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

    @GetMapping("/list/like")
    @ApiOperation(value = "메인 피드 좋아요순 조회")
    public ResponseEntity<BaseResponse<List<FeedResponse>>> findByLikeFeedList(@RequestParam Map<String,Integer> request){
        Integer pageNumber = request.get("pageNumber");

        List<FeedResponse> responses = feedService.findByLikeFeedList(pageNumber);

        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(responses)
                .build();

        return new ResponseEntity(response,HttpStatus.OK);
    }

    @GetMapping("/list/follow")
    @ApiOperation(value = "메인 피드 팔로우 조회")
    public ResponseEntity<BaseResponse<List<FeedResponse>>> findByFollowFeedList(
            @RequestParam Map<String,String> request){
        Integer pageNumber = Integer.valueOf(request.get("pageNumber"));
        Long memberId = Long.valueOf(request.get("memberId"));
        List<FeedResponse> responses = feedService.findByFollowFeedList(memberId,pageNumber);

        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(responses)
                .build();

        return new ResponseEntity(response,HttpStatus.OK);
    }

    @GetMapping("/list/view-count")
    @ApiOperation(value = "메인 피드 조회수순 조회")
    public ResponseEntity<BaseResponse<List<FeedResponse>>> findByViewCountFeedList(
            @RequestParam Map<String,String> request){
        Integer pageNumber = Integer.valueOf(request.get("pageNumber"));

        List<FeedResponse> responses = feedService.findByViewCountFeedList(pageNumber);

        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(responses)
                .build();

        return new ResponseEntity(response,HttpStatus.OK);
    }

    @PostMapping("/like")
    @ApiOperation(value = "피드 좋아요 메서드")
    public ResponseEntity<BaseResponse> createFeedLike(@RequestParam Map<String,Long> request){
        Long userId = request.get("userId");
        Long feedId = request.get("feedId");

        Boolean result = feedService.createFeedLike(feedId,userId);

        return responseResult(result);
    }

    @PostMapping("/scarp")
    @ApiOperation(value = "피드 스크랩 메서드")
    public ResponseEntity<BaseResponse> createFeedScrap(@RequestParam Map<String,Long> request){
        Long userId = request.get("userId");
        Long feedId = request.get("feedId");

        Boolean result = feedService.createFeedScrap(feedId,userId);

        return responseResult(result);
    }

    /**
     * 결과 객체 리턴해주는 메서드
     * @param result
     * @return
     */
    public ResponseEntity<BaseResponse> responseResult(Boolean result){

        if(result.booleanValue()){
            BaseResponse response = BaseResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("OK")
                    .build();

            return new ResponseEntity(response,HttpStatus.OK);
        }

        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message("BAD_REQUEST")
                .build();

        return new ResponseEntity(response,HttpStatus.BAD_REQUEST);
    }

}
