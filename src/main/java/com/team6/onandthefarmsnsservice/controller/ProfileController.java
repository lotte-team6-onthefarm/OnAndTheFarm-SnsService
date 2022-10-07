package com.team6.onandthefarmsnsservice.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team6.onandthefarmsnsservice.dto.profile.ProfileFeedDto;
import com.team6.onandthefarmsnsservice.dto.profile.ProfileMainFeedDto;
import com.team6.onandthefarmsnsservice.dto.profile.ProfileMainScrapDto;
import com.team6.onandthefarmsnsservice.dto.profile.ProfileMainWishDto;
import com.team6.onandthefarmsnsservice.service.FeedService;
import com.team6.onandthefarmsnsservice.utils.BaseResponse;
import com.team6.onandthefarmsnsservice.vo.FeedResponse;
import com.team6.onandthefarmsnsservice.vo.profile.ProfileMainFeedRequest;
import com.team6.onandthefarmsnsservice.vo.profile.ProfileMainFeedResponse;
import com.team6.onandthefarmsnsservice.vo.profile.ProfileMainScrapRequest;
import com.team6.onandthefarmsnsservice.vo.profile.ProfileMainScrapResponse;
import com.team6.onandthefarmsnsservice.vo.profile.ProfileMainWishRequest;
import com.team6.onandthefarmsnsservice.vo.profile.ProfileMainWishResponse;
import com.team6.onandthefarmsnsservice.vo.profile.ProfileFeedRequest;
import com.team6.onandthefarmsnsservice.vo.profile.product.ProductWishResponse;
import com.team6.onandthefarmsnsservice.vo.profile.product.WishProductListResponse;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/user/feed")
public class ProfileController {
	private FeedService feedService;

	@Autowired
	public ProfileController(FeedService feedService){
		this.feedService = feedService;
	}

	@GetMapping("/profile/main-feed")
	@ApiOperation(value = "프로필 메인 화면 feed 부분 조회")
	public ResponseEntity<BaseResponse<List<ProfileMainFeedResponse>>> getProfileMainFeed(@RequestBody ProfileMainFeedRequest profileMainFeedRequest){
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		ProfileMainFeedDto profileMainFeedDto = modelMapper.map(profileMainFeedRequest, ProfileMainFeedDto.class);

		List<ProfileMainFeedResponse> feedList = feedService.findByMemberFeedList(profileMainFeedDto);

		BaseResponse response = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("OK")
				.data(feedList)
				.build();

		return new ResponseEntity(response,HttpStatus.OK);
	}

	@GetMapping("/profile/main-scrap")
	@ApiOperation(value = "프로필 메인 화면 scrap 부분 조회")
	public ResponseEntity<BaseResponse<List<ProfileMainFeedResponse>>> getProfileMainScrap(@RequestBody ProfileMainScrapRequest profileMainScrapRequest){
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		ProfileMainScrapDto profileMainScrapDto = modelMapper.map(profileMainScrapRequest, ProfileMainScrapDto.class);
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
	public ResponseEntity<BaseResponse<List<ProfileMainWishResponse>>> getProfileMainWish(@RequestBody ProfileMainWishRequest profileMainWishRequest){
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		ProfileMainWishDto profileMainWishDto = modelMapper.map(profileMainWishRequest, ProfileMainWishDto.class);

		List<ProfileMainWishResponse> wishList = feedService.findByMemberWishList(profileMainWishDto);

		BaseResponse response = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("OK")
				.data(wishList)
				.build();

		return new ResponseEntity(response,HttpStatus.OK);
	}

	@GetMapping("/profile/feed")
	@ApiOperation(value = "프로필 피드 전체 조회")
	public ResponseEntity<BaseResponse<List<FeedResponse>>> getProfileFeedResponse(@RequestBody ProfileFeedRequest profileFeedRequest) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		ProfileFeedDto profileFeedDto = modelMapper.map(profileFeedRequest, ProfileFeedDto.class);

		List<FeedResponse> responses = feedService.findByRecentFeedListAndMemberId(profileFeedDto);

		BaseResponse response = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("OK")
				.data(responses)
				.build();

		return new ResponseEntity(response,HttpStatus.OK);
	}

	@GetMapping("/profile/scrap")
	@ApiOperation(value = "프로필 스크랩 전체 조회")
	public ResponseEntity<BaseResponse<List<FeedResponse>>> getProfileScrapFeedResponse(@RequestBody ProfileFeedRequest profileFeedRequest) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		ProfileFeedDto profileFeedDto = modelMapper.map(profileFeedRequest, ProfileFeedDto.class);

		List<FeedResponse> responses = feedService.findByRecentScrapFeedListAndMemberId(profileFeedDto);

		BaseResponse response = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("OK")
				.data(responses)
				.build();

		return new ResponseEntity(response,HttpStatus.OK);
	}

	@GetMapping("/profile/wish")
	@ApiOperation(value = "프로필 메인 화면 wish 전체 조회")
	public ResponseEntity<BaseResponse<List<WishProductListResponse>>> getProfileWishDetailList(@RequestBody ProfileMainWishRequest profileMainWishRequest){
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		ProfileMainWishDto profileMainWishDto = modelMapper.map(profileMainWishRequest, ProfileMainWishDto.class);

		List<WishProductListResponse> wishList = feedService.findByMemberWishDetailList(profileMainWishDto);

		BaseResponse response = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("OK")
				.data(wishList)
				.build();

		return new ResponseEntity(response,HttpStatus.OK);
	}
}
