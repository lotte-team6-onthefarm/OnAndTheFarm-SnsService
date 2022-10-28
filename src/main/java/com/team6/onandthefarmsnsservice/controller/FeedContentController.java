package com.team6.onandthefarmsnsservice.controller;

import com.team6.onandthefarmsnsservice.dto.FeedInfoDto;
import com.team6.onandthefarmsnsservice.service.FeedService;
import com.team6.onandthefarmsnsservice.utils.BaseResponse;
import com.team6.onandthefarmsnsservice.vo.feed.*;
import io.swagger.annotations.ApiOperation;
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

    /**
     * 현재 페이지, 전체 페이지 수 반환 필요
     * @param principal
     * @param pageNumber
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "메인 피드 최신순 조회")
    public ResponseEntity<BaseResponse<FeedResponseResult>> findByRecentFeedList(@ApiIgnore Principal principal,
                                                                                 @RequestParam Integer pageNumber){
        String[] principalInfo = principal.getName().split(" ");
        Long loginMemberId = Long.parseLong(principalInfo[0]);

        FeedResponseResult responses = feedService.findByRecentFeedList(pageNumber, loginMemberId);

        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(responses)
                .build();

        return new ResponseEntity(response,HttpStatus.OK);
    }

    @GetMapping("/list/like")
    @ApiOperation(value = "메인 피드 좋아요순 조회")
    public ResponseEntity<BaseResponse<FeedResponseResult>> findByLikeFeedList(@ApiIgnore Principal principal,
                                                                               @RequestParam Map<String,String> request){
        String[] principalInfo = principal.getName().split(" ");
        Long loginMemberId = Long.parseLong(principalInfo[0]);

        Integer pageNumber = Integer.valueOf(request.get("pageNumber"));

        FeedResponseResult responses = feedService.findByLikeFeedList(pageNumber, loginMemberId);

        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(responses)
                .build();

        return new ResponseEntity(response,HttpStatus.OK);
    }

    @GetMapping("/list/follow")
    @ApiOperation(value = "메인 피드 팔로우 조회")
    public ResponseEntity<BaseResponse<FeedResponseResult>> findByFollowFeedList(@ApiIgnore Principal principal,
                                                                                 @RequestParam Map<String,String> request){

        String[] principalInfo = principal.getName().split(" ");
        Long loginMemberId = Long.parseLong(principalInfo[0]);

        Integer pageNumber = Integer.valueOf(request.get("pageNumber"));
        FeedResponseResult responses = feedService.findByFollowFeedList(loginMemberId, pageNumber);

        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(responses)
                .build();

        return new ResponseEntity(response,HttpStatus.OK);
    }

    @GetMapping("/list/view-count")
    @ApiOperation(value = "메인 피드 조회수순 조회")
    public ResponseEntity<BaseResponse<FeedResponseResult>> findByViewCountFeedList(
            @ApiIgnore Principal principal,
            @RequestParam Map<String,String> request){

        String[] principalInfo = principal.getName().split(" ");
        Long loginMemberId = Long.parseLong(principalInfo[0]);

        Integer pageNumber = Integer.valueOf(request.get("pageNumber"));

        FeedResponseResult responses = feedService.findByViewCountFeedList(pageNumber, loginMemberId);

        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(responses)
                .build();

        return new ResponseEntity(response,HttpStatus.OK);
    }

    @PostMapping("/like")
    @ApiOperation(value = "피드 좋아요 메서드")
    public ResponseEntity<BaseResponse> createFeedLike(
            @ApiIgnore Principal principal, @RequestBody FeedRelatedRequest feedRelatedRequest){
        String[] principalInfo = principal.getName().split(" ");
        Long memberId = Long.parseLong(principalInfo[0]);

        Boolean result = feedService.createFeedLike(feedRelatedRequest.getFeedId(), memberId);

        return responseResult(result);
    }

    @PutMapping("/unlike")
    @ApiOperation(value = "피드 좋아요 취소 메서드")
    public ResponseEntity<BaseResponse> deleteFeedLike(
            @ApiIgnore Principal principal, @RequestBody FeedRelatedRequest feedRelatedRequest){
        String[] principalInfo = principal.getName().split(" ");
        Long memberId = Long.parseLong(principalInfo[0]);

        Boolean result = feedService.deleteFeedLike(feedRelatedRequest.getFeedId(), memberId);

        return responseResult(result);
    }

    @PostMapping("/scrap")
    @ApiOperation(value = "피드 스크랩 메서드")
    public ResponseEntity<BaseResponse> createFeedScrap(
            @ApiIgnore Principal principal, @RequestBody FeedRelatedRequest feedRelatedRequest){
        String[] principalInfo = principal.getName().split(" ");
        Long memberId = Long.parseLong(principalInfo[0]);

        Boolean result = feedService.createFeedScrap(feedRelatedRequest.getFeedId(), memberId);

        return responseResult(result);
    }

    @PutMapping("/unscrap")
    @ApiOperation(value = "피드 스크랩 취소 메서드")
    public ResponseEntity<BaseResponse> deleteFeedScrap(
            @ApiIgnore Principal principal, @RequestBody FeedRelatedRequest feedRelatedRequest){
        String[] principalInfo = principal.getName().split(" ");
        Long memberId = Long.parseLong(principalInfo[0]);

        Boolean result = feedService.deleteFeedScrap(feedRelatedRequest.getFeedId(), memberId);

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
