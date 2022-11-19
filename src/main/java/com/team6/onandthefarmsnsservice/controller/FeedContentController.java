package com.team6.onandthefarmsnsservice.controller;

import com.team6.onandthefarmsnsservice.dto.FeedInfoDto;
import com.team6.onandthefarmsnsservice.service.FeedService;
import com.team6.onandthefarmsnsservice.utils.BaseResponse;
import com.team6.onandthefarmsnsservice.vo.feed.*;
import com.team6.onandthefarmsnsservice.vo.product.AddableProductResponse;
import com.team6.onandthefarmsnsservice.vo.user.UserPointUpdateRequest;
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

    @PostMapping("/feed/upload")
    @ApiOperation("sns 피드 업로드")
    public ResponseEntity<BaseResponse> uploadFeed(@ApiIgnore Principal principal,
                                                   @RequestPart(value = "data", required = false) FeedUploadRequest feedUploadRequest,
                                                   @RequestPart(value = "images", required = false) List<MultipartFile> feedImages,
                                                   @RequestPart(value = "productData", required = false) FeedUploadProductRequest feedUploadProductRequest)
            throws Exception {

        FeedInfoDto feedInfoDto = new FeedInfoDto();
        feedInfoDto.setFeedTitle(feedUploadRequest.getFeedTitle());
        feedInfoDto.setFeedContent(feedUploadRequest.getFeedContent());
        feedInfoDto.setFeedTag(feedUploadRequest.getFeedTag());
        feedInfoDto.setFeedProductIdList(feedUploadProductRequest.getFeedProductIdList());
        feedInfoDto.setFeedImgSrcList(feedImages);

        String[] principalInfo = principal.getName().split(" ");
        Long memberId = Long.parseLong(principalInfo[0]);
        String memberRole = principalInfo[1];

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

    @GetMapping("/feed/detail")
    @ApiOperation("sns 피드 상세페이지")
    public ResponseEntity<BaseResponse<FeedDetailResponse>> findFeedDetail(@ApiIgnore Principal principal, @RequestParam Map<String, String> request){

        Long loginMemberId = 0L;
        Long feedId = Long.parseLong(request.get("feedId"));

        if(principal != null){
            String[] principalInfo = principal.getName().split(" ");
            loginMemberId = Long.parseLong(principalInfo[0]);
        }

        //조회수 증가
        Boolean isUpViewCount = feedService.upViewCount(feedId);
        if(!isUpViewCount){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        FeedDetailResponse feedDetailResponse = feedService.findFeedDetail(feedId, loginMemberId);

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

    @PutMapping("/feed/modify")
    @ApiOperation("sns 피드 수정")
    public ResponseEntity<BaseResponse<FeedDetailResponse>> modifyFeed(@ApiIgnore Principal principal,
                                                                       @RequestPart(value = "data", required = false) FeedModifyRequest feedModifyRequest,
                                                                       @RequestPart(value = "images", required = false) List<MultipartFile> feedImages,
                                                                       @RequestPart(value = "productData", required = false) FeedUploadProductRequest feedUploadProductRequest)
            throws Exception {

        FeedInfoDto feedInfoDto = new FeedInfoDto();
        feedInfoDto.setFeedId(feedModifyRequest.getFeedId());
        feedInfoDto.setFeedTitle(feedModifyRequest.getFeedTitle());
        feedInfoDto.setFeedContent(feedModifyRequest.getFeedContent());
        feedInfoDto.setFeedTag(feedModifyRequest.getFeedTag());
        feedInfoDto.setFeedProductIdList(feedUploadProductRequest.getFeedProductIdList());
        feedInfoDto.setFeedImgSrcList(feedImages);
        feedInfoDto.setDeleteImg(feedModifyRequest.getDeleteImg());

        String[] principalInfo = principal.getName().split(" ");
        Long memberId = Long.parseLong(principalInfo[0]);

        Long feedId = feedService.modifyFeed(memberId, feedInfoDto);

        BaseResponse baseResponse = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("find feed success")
                .data(feedId)
                .build();
        if(feedId == null){
            baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("find feed fail")
                    .build();
            return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(baseResponse, HttpStatus.OK);
    }

    @PutMapping("/feed/delete")
    @ApiOperation("sns 피드 삭제")
    public ResponseEntity<BaseResponse<FeedDetailResponse>> deleteFeed(@ApiIgnore Principal principal, @RequestBody FeedDeleteRequest feedDeleteRequest){

        String[] principalInfo = principal.getName().split(" ");
        Long memberId = Long.parseLong(principalInfo[0]);

        Long deletedFeedId = feedService.deleteFeed(memberId, feedDeleteRequest.getFeedId());

        BaseResponse baseResponse = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("delete feed success")
                .data(deletedFeedId)
                .build();
        if(deletedFeedId == null){
            baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("delete feed fail")
                    .build();
            return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(baseResponse, HttpStatus.OK);
    }


    @GetMapping("/feed/product")
    @ApiOperation("sns 피드에 등록 가능한 상품 목록 조회")
    public ResponseEntity<BaseResponse<List<AddableProductResponse>>> findAddableProduct(@ApiIgnore Principal principal){

        String[] principalInfo = principal.getName().split(" ");
        Long memberId = Long.parseLong(principalInfo[0]);
        String memberRole = principalInfo[1];

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

    @PutMapping("/feed/share")
    @ApiOperation("sns 피드 공유 시 공유 카운트 업데이트하는 메서드")
    public ResponseEntity<BaseResponse> upShareCount(@RequestBody FeedRelatedRequest feedRelatedRequest){

        Boolean isUpShareCount = feedService.upShareCount(feedRelatedRequest.getFeedId());

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

    @PutMapping("/feed/share/point")
    @ApiOperation("공유된 url로 feed 접근 시 feed 작성자에게 포인트 지급")
    public ResponseEntity<BaseResponse> updateSharePoint(@RequestBody UserPointUpdateRequest userPointUpdateRequest){

        Boolean successStatus = feedService.updateSharePoint(userPointUpdateRequest.getFeedNumber());

        if(!successStatus){
            BaseResponse response = BaseResponse.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("update user point fail")
                    .build();

            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }

        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("update user point success")
                .build();

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/feed/list/tag")
    @ApiOperation("tag 별로 조회")
    public ResponseEntity<BaseResponse<FeedResponseResult>> findByFeedTag(@ApiIgnore Principal principal,
                                                                          @RequestParam String feedTagName,
                                                                          @RequestParam Integer pageNumber){
        Long loginMemberId = null;
        if(principal != null){
            String[] principalInfo = principal.getName().split(" ");
            loginMemberId = Long.parseLong(principalInfo[0]);
        }

        FeedResponseResult feedResponses = feedService.findByFeedTag(feedTagName, pageNumber, loginMemberId);

        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(feedResponses)
                .build();

        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**
     * 현재 페이지, 전체 페이지 수 반환 필요
     * @param principal
     * @param pageNumber
     * @return
     */
    @GetMapping("/feed/list/orderby")
    @ApiOperation(value = "메인 피드 최신순 조회")
    public ResponseEntity<BaseResponse<FeedResponseResult>> findByRecentFeedList(@ApiIgnore Principal principal,
                                                                                 @RequestParam Integer pageNumber){
        Long loginMemberId = null;
        if(principal != null){
            String[] principalInfo = principal.getName().split(" ");
            loginMemberId = Long.parseLong(principalInfo[0]);
        }

        FeedResponseResult responses = feedService.findByRecentFeedList(pageNumber, loginMemberId);

        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(responses)
                .build();

        return new ResponseEntity(response,HttpStatus.OK);
    }

    @GetMapping("/feed/list/orderby/like")
    @ApiOperation(value = "메인 피드 좋아요순 조회")
    public ResponseEntity<BaseResponse<FeedResponseResult>> findByLikeFeedList(@ApiIgnore Principal principal,
                                                                               @RequestParam Map<String,String> request){
        Long loginMemberId = null;
        if(principal != null){
            String[] principalInfo = principal.getName().split(" ");
            loginMemberId = Long.parseLong(principalInfo[0]);
        }

        Integer pageNumber = Integer.valueOf(request.get("pageNumber"));

        FeedResponseResult responses = feedService.findByLikeFeedList(pageNumber, loginMemberId);

        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(responses)
                .build();

        return new ResponseEntity(response,HttpStatus.OK);
    }

    @GetMapping("/feed/list/orderby/follow")
    @ApiOperation(value = "메인 피드 팔로우 조회")
    public ResponseEntity<BaseResponse<FeedResponseResult>> findByFollowFeedList(@ApiIgnore Principal principal,
                                                                                 @RequestParam Map<String,String> request){

        Long loginMemberId = null;
        if(principal != null){
            String[] principalInfo = principal.getName().split(" ");
            loginMemberId = Long.parseLong(principalInfo[0]);
        }
        else{
            BaseResponse response = BaseResponse.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("BAD_REQUEST")
                    .build();

            return new ResponseEntity(response,HttpStatus.BAD_REQUEST);
        }

        Integer pageNumber = Integer.valueOf(request.get("pageNumber"));
        FeedResponseResult responses = feedService.findByFollowFeedList(loginMemberId, pageNumber);

        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(responses)
                .build();

        return new ResponseEntity(response,HttpStatus.OK);
    }

    @GetMapping("/feed/list/orderby/view-count")
    @ApiOperation(value = "메인 피드 조회수순 조회")
    public ResponseEntity<BaseResponse<FeedResponseResult>> findByViewCountFeedList(
            @ApiIgnore Principal principal,
            @RequestParam Map<String,String> request){

        Long loginMemberId = null;
        if(principal != null){
            String[] principalInfo = principal.getName().split(" ");
            loginMemberId = Long.parseLong(principalInfo[0]);
        }

        Integer pageNumber = Integer.valueOf(request.get("pageNumber"));

        FeedResponseResult responses = feedService.findByViewCountFeedList(pageNumber, loginMemberId);

        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(responses)
                .build();

        return new ResponseEntity(response,HttpStatus.OK);
    }

    @PostMapping("/feed/like")
    @ApiOperation(value = "피드 좋아요 메서드")
    public ResponseEntity<BaseResponse> createFeedLike(
            @ApiIgnore Principal principal, @RequestBody FeedRelatedRequest feedRelatedRequest){
        String[] principalInfo = principal.getName().split(" ");
        Long memberId = Long.parseLong(principalInfo[0]);

        Boolean result = feedService.createFeedLike(feedRelatedRequest.getFeedId(), memberId);

        return responseResult(result);
    }

    @PutMapping("/feed/unlike")
    @ApiOperation(value = "피드 좋아요 취소 메서드")
    public ResponseEntity<BaseResponse> deleteFeedLike(
            @ApiIgnore Principal principal, @RequestBody FeedRelatedRequest feedRelatedRequest){
        String[] principalInfo = principal.getName().split(" ");
        Long memberId = Long.parseLong(principalInfo[0]);

        Boolean result = feedService.deleteFeedLike(feedRelatedRequest.getFeedId(), memberId);

        return responseResult(result);
    }

    @PostMapping("/feed/scrap")
    @ApiOperation(value = "피드 스크랩 메서드")
    public ResponseEntity<BaseResponse> createFeedScrap(
            @ApiIgnore Principal principal, @RequestBody FeedRelatedRequest feedRelatedRequest){
        String[] principalInfo = principal.getName().split(" ");
        Long memberId = Long.parseLong(principalInfo[0]);

        Boolean result = feedService.createFeedScrap(feedRelatedRequest.getFeedId(), memberId);

        return responseResult(result);
    }

    @PutMapping("/feed/unscrap")
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

}
