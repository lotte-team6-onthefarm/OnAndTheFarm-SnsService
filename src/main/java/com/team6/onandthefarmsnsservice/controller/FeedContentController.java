package com.team6.onandthefarmsnsservice.controller;

import com.team6.onandthefarmsnsservice.dto.FeedDto;
import com.team6.onandthefarmsnsservice.dto.FeedInfoDto;
import com.team6.onandthefarmsnsservice.feignclient.ProductServiceClient;
import com.team6.onandthefarmsnsservice.service.FeedService;
import com.team6.onandthefarmsnsservice.utils.BaseResponse;
import com.team6.onandthefarmsnsservice.vo.FeedRequest;
import com.team6.onandthefarmsnsservice.vo.FeedResponse;
import com.team6.onandthefarmsnsservice.vo.feed.AddableProductResponse;
import com.team6.onandthefarmsnsservice.vo.feed.FeedDetailResponse;
import com.team6.onandthefarmsnsservice.vo.feed.FeedUploadProductRequest;
import com.team6.onandthefarmsnsservice.vo.feed.FeedUploadRequest;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user/sns")
public class FeedContentController {

    private final FeedService feedService;

    @Autowired
    public FeedContentController(FeedService feedService){
        this.feedService = feedService;
    }

    @PostMapping("/upload")
    @ApiOperation("sns 피드 업로드")
    public ResponseEntity<BaseResponse> uploadFeed(@ApiIgnore Principal principal, @RequestPart FeedUploadRequest feedUploadRequest, @RequestPart List<MultipartFile> feedImages, @RequestPart FeedUploadProductRequest feedUploadProductRequest){

        FeedInfoDto feedInfoDto = new FeedInfoDto();
        feedInfoDto.setFeedTitle(feedUploadRequest.getFeedTitle());
        feedInfoDto.setFeedContent(feedUploadRequest.getFeedContent());
        feedInfoDto.setFeedTag(feedUploadRequest.getFeedTag());
        feedInfoDto.setFeedProductIdList(feedUploadProductRequest.getFeedProductIdList());
        feedInfoDto.setFeedImgSrcList(feedImages);

        Long memberId = Long.parseLong(principal.getName());
        String memberRole = null;

        Long feedId = feedService.uploadFeed(memberId, memberRole, feedInfoDto);

        BaseResponse baseResponse = BaseResponse.builder()
                .httpStatus(HttpStatus.CREATED)
                .message("feed upload success")
                .data(feedId)
                .build();
        if(feedId == null){
            baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("feed upload fail")
                    .build();
            return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(baseResponse, HttpStatus.OK);
    }

    @GetMapping("/detail")
    @ApiOperation("sns 피드 상세페이지")
    public ResponseEntity<BaseResponse<FeedDetailResponse>> findFeedDetail(@ApiIgnore Principal principal, @RequestParam Long feedId){

        //조회수 증가
        Boolean isUpViewCount = feedService.upViewCount(feedId);
        if(!isUpViewCount){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Long memberId = Long.parseLong(principal.getName());
        FeedDetailResponse feedDetailResponse = feedService.findFeedDetail(feedId, memberId);

        BaseResponse baseResponse = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("find feed success")
                .data(feedDetailResponse)
                .build();
        if(feedDetailResponse == null){
            baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("find feed fail")
                    .build();
            return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(baseResponse, HttpStatus.OK);
    }

    @GetMapping("/product")
    @ApiOperation("sns 피드에 등록 가능한 상품 목록 조회")
    public ResponseEntity<BaseResponse<List<AddableProductResponse>>> findAddableProduct(@ApiIgnore Principal principal){

        Long memberId = null;
        String memberRole = null;

        List<AddableProductResponse> addableProductList = feedService.findAddableProducts(memberId, memberRole);

        BaseResponse baseResponse = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("find product success")
                .data(addableProductList)
                .build();
        if(addableProductList == null){
            baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("find product fail")
                    .build();
            return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(baseResponse, HttpStatus.OK);
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
    @GetMapping("/share")
    @ApiOperation("sns 피드 공유 시 공유 카운트 업데이트하는 메서드")
    public ResponseEntity<BaseResponse> upShareCount(@RequestParam Long feedId){

        Boolean isUpShareCount = feedService.upShareCount(feedId);

        BaseResponse baseResponse = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("up share count success")
                .build();
        if(!isUpShareCount){
            baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("up share count fail")
                    .build();
            return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(baseResponse, HttpStatus.OK);
    }

}
