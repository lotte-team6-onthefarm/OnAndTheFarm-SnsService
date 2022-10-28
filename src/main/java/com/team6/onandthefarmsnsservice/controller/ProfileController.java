package com.team6.onandthefarmsnsservice.controller;


import com.team6.onandthefarmsnsservice.dto.profile.*;
import com.team6.onandthefarmsnsservice.vo.profile.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import com.team6.onandthefarmsnsservice.service.FeedService;
import com.team6.onandthefarmsnsservice.utils.BaseResponse;
import com.team6.onandthefarmsnsservice.vo.feed.FeedResponseResult;

@RestController
@RequestMapping("/api/user/sns")
public class ProfileController {
	private FeedService feedService;

	@Autowired
	public ProfileController(FeedService feedService){
		this.feedService = feedService;
	}

	@GetMapping ("/profile/main-feed")
	@ApiOperation(value = "프로필 메인 화면 feed 부분 조회")
	public ResponseEntity<BaseResponse<List<ProfileMainFeedResponse>>> getProfileMainFeed(@ApiIgnore Principal principal,
			@RequestParam Map<String, String> request){

		ProfileMainFeedDto profileMainFeedDto = new ProfileMainFeedDto();

		Long memberId = null;
		if(request.containsKey("memberId")){
			profileMainFeedDto.setMemberId(Long.parseLong(request.get("memberId")));
		}
		else {
			String[] principalInfo = principal.getName().split(" ");
			memberId = Long.parseLong(principalInfo[0]);
			profileMainFeedDto.setMemberId(memberId);
		}

		List<ProfileMainFeedResponse> feedList = feedService.findFeedListByMember(profileMainFeedDto);

		BaseResponse response = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("OK")
				.data(feedList)
				.build();

		return new ResponseEntity(response,HttpStatus.OK);
	}

	@GetMapping("/profile/main-scrap")
	@ApiOperation(value = "프로필 메인 화면 scrap 부분 조회")
	public ResponseEntity<BaseResponse<List<ProfileMainScrapResponse>>> getProfileMainScrap(@ApiIgnore Principal principal,
			@RequestParam Map<String, String> request){

		ProfileMainScrapDto profileMainScrapDto = new ProfileMainScrapDto();

		Long memberId = null;
		if(request.containsKey("memberId")) {
			profileMainScrapDto.setMemberId(Long.parseLong(request.get("memberId")));
		}
		else {
			String[] principalInfo = principal.getName().split(" ");
			memberId = Long.parseLong(principalInfo[0]);
			profileMainScrapDto.setMemberId(memberId);
		}

		List<ProfileMainScrapResponse> scrapList = feedService.findByMemberScrapList(profileMainScrapDto);

		BaseResponse response = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("OK")
				.data(scrapList)
				.build();

		return new ResponseEntity(response,HttpStatus.OK);
	}

	@GetMapping("/profile/main-wish")
	@ApiOperation(value = "프로필 메인 화면 wish 부분 조회")
	public ResponseEntity<BaseResponse<List<ProfileMainWishResponse>>> getProfileMainWish(@ApiIgnore Principal principal,
			@RequestParam Map<String, String> request){

		ProfileMainWishDto profileMainWishDto = new ProfileMainWishDto();

		Long memberId = null;
		if(request.containsKey("memberId")) {
			profileMainWishDto.setMemberId(Long.parseLong(request.get("memberId")));
		}
		else {
			String[] principalInfo = principal.getName().split(" ");
			memberId = Long.parseLong(principalInfo[0]);
			profileMainWishDto.setMemberId(memberId);
		}

		List<ProfileMainWishResponse> wishList = feedService.findWishListByMember(profileMainWishDto);

		BaseResponse response = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("OK")
				.data(wishList)
				.build();

		return new ResponseEntity(response,HttpStatus.OK);
	}

	@GetMapping("/profile/feed")
	@ApiOperation(value = "프로필 피드 전체 조회")
	public ResponseEntity<BaseResponse<FeedResponseResult>> getProfileFeedResponse(@ApiIgnore Principal principal,
			@RequestParam Map<String, String> request) {


		String[] principalInfo = principal.getName().split(" ");
		Long loginMemberId = Long.parseLong(principalInfo[0]);

		ProfileFeedDto profileFeedDto = new ProfileFeedDto();
		profileFeedDto.setPageNumber(Integer.parseInt(request.get("pageNumber")));
		profileFeedDto.setLoginMemberId(loginMemberId);

		if(request.containsKey("memberId")) {
			profileFeedDto.setMemberId(Long.parseLong(request.get("memberId")));
		}
		else {
			profileFeedDto.setMemberId(loginMemberId);
		}

		FeedResponseResult responses = feedService.findByRecentFeedListAndMemberId(profileFeedDto);

		BaseResponse response = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("OK")
				.data(responses)
				.build();

		return new ResponseEntity(response,HttpStatus.OK);
	}

	@GetMapping("/profile/scrap")
	@ApiOperation(value = "프로필 스크랩 전체 조회")
	public ResponseEntity<BaseResponse<FeedResponseResult>> getProfileScrapFeedResponse(@ApiIgnore Principal principal,
			@RequestParam Map<String, String> request) {


		String[] principalInfo = principal.getName().split(" ");
		Long loginMemberId =  Long.parseLong(principalInfo[0]);

		ProfileFeedDto profileFeedDto = new ProfileFeedDto();
		profileFeedDto.setPageNumber(Integer.parseInt(request.get("pageNumber")));
		profileFeedDto.setLoginMemberId(loginMemberId);

		if(request.containsKey("memberId")) {
			profileFeedDto.setMemberId(Long.parseLong(request.get("memberId")));
		}
		else {
			profileFeedDto.setMemberId(loginMemberId);
		}

		FeedResponseResult responses = feedService.findByRecentScrapFeedListAndMemberId(profileFeedDto);

		BaseResponse response = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("OK")
				.data(responses)
				.build();

		return new ResponseEntity(response,HttpStatus.OK);
	}

	@GetMapping("/profile/wish")
	@ApiOperation(value = "프로필 메인 화면 wish 전체 조회")
	public ResponseEntity<BaseResponse<WishProductListResult>> getProfileWishDetailList(@ApiIgnore Principal principal,
			@RequestParam Map<String, String> request){

		ProfileMainWishDto profileMainWishDto = new ProfileMainWishDto();
		profileMainWishDto.setPageNumber(Integer.parseInt(request.get("pageNumber")));

		Long memberId = null;
		if(request.containsKey("memberId")) {
			profileMainWishDto.setMemberId(Long.parseLong(request.get("memberId")));
		}
		else{
			String[] principalInfo = principal.getName().split(" ");
			memberId = Long.parseLong(principalInfo[0]);
			profileMainWishDto.setMemberId(memberId);
		}

		WishProductListResult wishList = feedService.findByMemberWishDetailList(profileMainWishDto);

		BaseResponse response = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("OK")
				.data(wishList)
				.build();

		return new ResponseEntity(response,HttpStatus.OK);
	}

	@GetMapping("/profile/count")
	@ApiOperation(value = "멤버의 피드 수,스크랩 수, 좋아요 수 조회")
	public ResponseEntity<BaseResponse<MemberProfileCountResponse>> getFeedScrapLikeCount(@ApiIgnore Principal principal,
																						  @RequestParam Map<String, String> request){

		MemberProfileDto memberProfileDto = new MemberProfileDto();

		String[] principalInfo = principal.getName().split(" ");
		Long loginId = Long.parseLong(principalInfo[0]);
		String loginRole = principalInfo[1];

		if(request.containsKey("memberId")) {
			memberProfileDto.setMemberId(Long.parseLong(request.get("memberId")));
			memberProfileDto.setMemberRole(request.get("memberRole"));
		}else{
			memberProfileDto.setMemberId(loginId);
			memberProfileDto.setMemberRole(loginRole);
		}

		MemberProfileCountResponse memberProfileCountResponse = feedService.getFeedScrapWishCount(memberProfileDto);

		BaseResponse response = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("OK")
				.data(memberProfileCountResponse)
				.build();

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
