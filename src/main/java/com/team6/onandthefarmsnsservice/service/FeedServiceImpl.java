package com.team6.onandthefarmsnsservice.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.team6.onandthefarmsnsservice.vo.feed.FeedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.team6.onandthefarmsnsservice.dto.FeedInfoDto;
import com.team6.onandthefarmsnsservice.dto.profile.ProfileFeedDto;
import com.team6.onandthefarmsnsservice.dto.profile.ProfileMainFeedDto;
import com.team6.onandthefarmsnsservice.dto.profile.ProfileMainScrapDto;
import com.team6.onandthefarmsnsservice.dto.profile.ProfileMainWishDto;
import com.team6.onandthefarmsnsservice.entity.Feed;
import com.team6.onandthefarmsnsservice.entity.FeedImage;
import com.team6.onandthefarmsnsservice.entity.FeedImageProduct;
import com.team6.onandthefarmsnsservice.entity.FeedLike;
import com.team6.onandthefarmsnsservice.entity.FeedTag;
import com.team6.onandthefarmsnsservice.entity.Scrap;
import com.team6.onandthefarmsnsservice.feignclient.MemberServiceClient;
import com.team6.onandthefarmsnsservice.feignclient.ProductServiceClient;
import com.team6.onandthefarmsnsservice.repository.FeedImageProductRepository;
import com.team6.onandthefarmsnsservice.repository.FeedImageRepository;
import com.team6.onandthefarmsnsservice.repository.FeedLikeRepository;
import com.team6.onandthefarmsnsservice.repository.FeedRepository;
import com.team6.onandthefarmsnsservice.repository.FeedTagRepository;
import com.team6.onandthefarmsnsservice.repository.ScrapRepository;
import com.team6.onandthefarmsnsservice.utils.DateUtils;
import com.team6.onandthefarmsnsservice.vo.feed.AddableProductResponse;
import com.team6.onandthefarmsnsservice.vo.feed.FeedDetailResponse;
import com.team6.onandthefarmsnsservice.vo.feed.FeedResponseResult;
import com.team6.onandthefarmsnsservice.vo.feed.imageProduct.ImageInfo;
import com.team6.onandthefarmsnsservice.vo.feed.imageProduct.ImageProductInfo;
import com.team6.onandthefarmsnsservice.vo.feed.imageProduct.ImageProductResponse;
import com.team6.onandthefarmsnsservice.vo.product.ProductVo;
import com.team6.onandthefarmsnsservice.vo.product.WishVo;
import com.team6.onandthefarmsnsservice.vo.profile.MemberProfileCountResponse;
import com.team6.onandthefarmsnsservice.vo.profile.ProfileMainFeedResponse;
import com.team6.onandthefarmsnsservice.vo.profile.ProfileMainScrapResponse;
import com.team6.onandthefarmsnsservice.vo.profile.ProfileMainWishResponse;
import com.team6.onandthefarmsnsservice.vo.profile.WishProductListResponse;
import com.team6.onandthefarmsnsservice.vo.profile.WishProductListResult;
import com.team6.onandthefarmsnsservice.vo.user.Following;
import com.team6.onandthefarmsnsservice.vo.user.SellerVo;
import com.team6.onandthefarmsnsservice.vo.user.UserVo;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class FeedServiceImpl implements FeedService {

	private final int pageContentNumber = 8;

	private final FeedRepository feedRepository;

//	private final MemberServiceClient memberServiceClient;
//
//	private final ProductServiceClient productServiceClient;
//
//	private final OrderServiceClient orderServiceClient;

	private final UserRepository userRepository;

	private final SellerRepository sellerRepository;

	private final FollowingRepository followingRepository;

	private final ProductRepository productRepository;

	private final ProductWishRepository productWishRepository;

	private final OrderRepository orderRepository;

	private final OrderProductRepository orderProductRepository;

	private final FeedImageRepository feedImageRepository;

	private final FeedLikeRepository feedLikeRepository;

	private final FeedTagRepository feedTagRepository;

	private final ScrapRepository scrapRepository;

	private final MemberServiceClient memberServiceClient;

	private final ProductServiceClient productServiceClient;

	private final FeedImageProductRepository feedImageProductRepository;

	private final ReviewRepository reviewRepository;

	private final S3Upload s3Upload;

	private DateUtils dateUtils;
	Environment env;


	@Autowired
	public FeedServiceImpl(FeedRepository feedRepository,
						   FeedImageRepository feedImageRepository,
						   FeedLikeRepository feedLikeRepository,
						   ScrapRepository scrapRepository,
						   FeedImageProductRepository feedImageProductRepository,
						   FeedTagRepository feedTagRepository,
						   MemberServiceClient memberServiceClient,
						   ProductServiceClient productServiceClient,
						   UserRepository userRepository,
						   SellerRepository sellerRepository,
						   FollowingRepository followingRepository,
						   ProductRepository productRepository,
						   ProductWishRepository productWishRepository,
						   OrderRepository orderRepository,
						   OrderProductRepository orderProductRepository,
						   ReviewRepository reviewRepository,
						   S3Upload s3Upload,
						   DateUtils dateUtils,
						   Environment env) {
		this.memberServiceClient = memberServiceClient;
		this.productServiceClient = productServiceClient;
		this.feedLikeRepository = feedLikeRepository;
		this.scrapRepository = scrapRepository;
		this.feedRepository = feedRepository;
		this.feedImageRepository = feedImageRepository;
		this.feedImageProductRepository = feedImageProductRepository;
		this.feedTagRepository = feedTagRepository;
		this.userRepository = userRepository;
		this.sellerRepository = sellerRepository;
		this.followingRepository = followingRepository;
		this.productRepository = productRepository;
		this.productWishRepository = productWishRepository;
		this.orderRepository = orderRepository;
		this.orderProductRepository = orderProductRepository;
		this.reviewRepository = reviewRepository;
		this.s3Upload = s3Upload;
		this.dateUtils = dateUtils;
		this.env = env;
	}

	/**
	 * 피드 업로드하는 메서드
	 *
	 * @param memberId, memberRole, feedInfoDto
	 * @return feedId
	 */
	@Override
	public Long uploadFeed(Long memberId, String memberRole, FeedInfoDto feedInfoDto) throws IOException {

		Feed feed = new Feed();
		feed.setMemberId(memberId);
		feed.setMemberRole(memberRole);
		feed.setFeedTitle(feedInfoDto.getFeedTitle());
		feed.setFeedContent(feedInfoDto.getFeedContent());
		feed.setFeedViewCount(0);
		feed.setFeedLikeCount(0);
		feed.setFeedShareCount(0);
		feed.setFeedScrapCount(0);
		feed.setFeedCommentCount(0);
		feed.setFeedStatus(true);
		feed.setFeedCreateAt(dateUtils.transDate(env.getProperty("dateutils.format")));

		//피드 추가
		Feed savedFeed = feedRepository.save(feed);

		//피드 태그 추가
		if(feedInfoDto.getFeedTag()!=null) {
			for (String tag : feedInfoDto.getFeedTag()) {
				FeedTag feedTag = new FeedTag();
				feedTag.setFeed(savedFeed);
				feedTag.setFeedTagName(tag);
				feedTagRepository.save(feedTag);
			}
		}

		int imageIndex = 0;
		for (MultipartFile imageSrc : feedInfoDto.getFeedImgSrcList()) {
			//피드 이미지 추가
			String url = s3Upload.feedUpload(imageSrc);

			FeedImage feedImage = new FeedImage();
			feedImage.setFeed(savedFeed);
			feedImage.setFeedImageSrc(url);
			FeedImage saveFeedImage = feedImageRepository.save(feedImage);

			if(feedInfoDto.getFeedProductIdList() != null) {
				for (ImageProductInfo imageProduct : feedInfoDto.getFeedProductIdList()) {
					if (imageProduct.getImageIndex() == imageIndex) {
						FeedImageProduct feedImageProduct = new FeedImageProduct();
						feedImageProduct.setFeedImage(saveFeedImage);
						feedImageProduct.setProductId(imageProduct.getProductId());

						//피드 이미지 별 상품 추가
						FeedImageProduct savedFeedImageProduct = feedImageProductRepository.save(feedImageProduct);
					}
				}
			}
			imageIndex++;
		}

		return savedFeed.getFeedId();
	}

	/**
	 * 피드 업로드할 때 추가 가능한 상품을 조회하는 메서드
	 *
	 * @param memberId, memberRole
	 * @return List<AddableProductResponse>
	 */
	@Override
	public List<AddableProductResponse> findAddableProducts(Long memberId, String memberRole) {

		List<AddableProductResponse> addableProductList = new ArrayList<>();

		if (memberRole.equals("user")) {
			List<Orders> ordersList = orderRepository.findWithOrderAndOrdersStatus(memberId);
			List<Long> productIdList = new ArrayList<>();
			for (Orders orders : ordersList) {
				//orderproduct받아와서 해당 productID로 조회하기
				List<OrderProduct> orderProductList = orderProductRepository.findByOrders(orders);
				for (OrderProduct orderProduct : orderProductList) {
					if(!productIdList.contains(orderProduct.getProductId())) {
						productIdList.add(orderProduct.getProductId());

						Optional<Product> savedProduct = productRepository.findById(orderProduct.getProductId());
						Product product = savedProduct.get();

						Optional<SellerVo> savedSeller = sellerRepository.findById(product.getSeller().getSellerId());

						AddableProductResponse addableProductResponse = AddableProductResponse.builder()
								.productId(product.getProductId())
								.productName(product.getProductName())
								.productPrice(product.getProductPrice())
								.productMainImgSrc(product.getProductMainImgSrc())
								.sellerShopName(savedSeller.get().getSellerShopName())
								.build();

						addableProductList.add(addableProductResponse);
					}
				}
			}
		} else if (memberRole.equals("seller")) {
			Optional<SellerVo> savedSeller = sellerRepository.findById(memberId);
			SellerVo sellerVo = savedSeller.get();


			List<Product> productList = productRepository.findBySeller(sellerVo);

			for (Product product : productList) {
				AddableProductResponse addableProductResponse = AddableProductResponse.builder()
						.productId(product.getProductId())
						.productName(product.getProductName())
						.productPrice(product.getProductPrice())
						.productMainImgSrc(product.getProductMainImgSrc())
						.sellerShopName(savedSeller.get().getSellerShopName())
						.build();

				addableProductList.add(addableProductResponse);
			}
		}

		return addableProductList;
	}

	/**
	 * 피드 상세 페이지 조회하는 메서드
	 *
	 * @param feedId
	 * @return FeedDetailResponse
	 */
	@Override
	public FeedDetailResponse findFeedDetail(Long feedId, Long loginMemberId) {

		FeedDetailResponse feedDetailResponse = new FeedDetailResponse();

		Optional<Feed> savedFeed = feedRepository.findByIdAndStatus(feedId);
		if (savedFeed.isPresent()) {
			Feed feedEntity = savedFeed.get();

			// feed image 및 image 별 product
			List<ImageInfo> imageInfoList = new ArrayList<>();
			List<ImageProductResponse> imageProductInfoList = new ArrayList<>();

			List<FeedImage> savedFeedImageList = feedImageRepository.findByFeed(feedEntity);
			for (FeedImage feedImage : savedFeedImageList) {
				ImageInfo imageInfo = ImageInfo.builder()
						.feedImageId(feedImage.getFeedImageId())
						.feedImageSrc(feedImage.getFeedImageSrc())
						.build();
				imageInfoList.add(imageInfo);

				List<FeedImageProduct> savedFeedImageProductList = feedImageProductRepository.findByFeedImage(
						feedImage);
				for (FeedImageProduct feedImageProduct : savedFeedImageProductList) {
					Optional<Product> product = productRepository.findById(feedImageProduct.getProductId());

					ImageProductResponse imageProductResponse = ImageProductResponse.builder()
							.feedImageId(feedImage.getFeedImageId())
							.productId(feedImageProduct.getProductId())
							.productPrice(product.get().getProductPrice())
							.productName(product.get().getProductName())
							.productMainImgSrc(product.get().getProductMainImgSrc())
							.sellerName(product.get().getSeller().getSellerName())
							.build();
					imageProductInfoList.add(imageProductResponse);
				}
			}

			// feed 별 tag
			List<FeedTag> feedTagList = feedTagRepository.findByFeed(feedEntity);

			feedDetailResponse = FeedDetailResponse.builder()
					.feedId(feedEntity.getFeedId())
					.feedTitle(feedEntity.getFeedTitle())
					.feedContent(feedEntity.getFeedContent())
					.feedViewCount(feedEntity.getFeedViewCount())
					.feedLikeCount(feedEntity.getFeedLikeCount())
					.feedShareCount(feedEntity.getFeedShareCount())
					.feedScrapCount(feedEntity.getFeedScrapCount())
					.feedCommentCount(feedEntity.getFeedCommentCount())
					.feedCreateAt(feedEntity.getFeedCreateAt())
					.feedUpdateAt(feedEntity.getFeedUpdateAt())
					.feedImageList(imageInfoList)
					.feedImageProductList(imageProductInfoList)
					.feedTag(feedTagList)
					.isModifiable(false)
					.feedLikeStatus(false)
					.scrapStatus(false)
					.followStatus(false)
					.build();

			// feed 작성자와 로그인한 사용자가 같은지 여부
			if (savedFeed.get().getMemberId() == loginMemberId) {
				feedDetailResponse.setIsModifiable(true);
			}

			// feed 작성자와 로그인한 사용자의 팔로우 여부
			Optional<Following> followingStatus = followingRepository.findByFollowingMemberIdAndFollowerMemberId(loginMemberId, savedFeed.get().getMemberId());
			if(followingStatus.isPresent()){
				feedDetailResponse.setFollowStatus(true);
			}

			// feed에 대한 스크랩, 좋아요 여부
			Optional<FeedLike> feedLike = feedLikeRepository.findByFeedAndMemberId(savedFeed.get(), loginMemberId);
			if (feedLike.isPresent()) {
				feedDetailResponse.setFeedLikeStatus(true);
			}
			Optional<Scrap> scrap = scrapRepository.findByFeedAndMemberId(savedFeed.get(), loginMemberId);
			if (scrap.isPresent()) {
				feedDetailResponse.setScrapStatus(true);
			}

			// feed 작성자의 이름과 프로필 이미지
			if (savedFeed.get().getMemberRole().equals("user")) {
				Optional<UserVo> savedUser = userRepository.findById(savedFeed.get().getMemberId());
				feedDetailResponse.setMemberId(savedFeed.get().getMemberId());
				feedDetailResponse.setMemberRole(savedFeed.get().getMemberRole());
				feedDetailResponse.setMemberName(savedUser.get().getUserName());
				feedDetailResponse.setMemberProfileImg(savedUser.get().getUserProfileImg());
			} else {
				Optional<SellerVo> savedSeller = sellerRepository.findById(savedFeed.get().getMemberId());
				feedDetailResponse.setMemberId(savedFeed.get().getMemberId());
				feedDetailResponse.setMemberRole(savedFeed.get().getMemberRole());
				feedDetailResponse.setMemberName(savedSeller.get().getSellerName());
				feedDetailResponse.setMemberProfileImg(savedSeller.get().getSellerProfileImg());
			}

		}

		return feedDetailResponse;
	}

	/**
	 * 피드 수정하는 메서드
	 *
	 * @param memberId
	 * @return FeedDetailResponse
	 */
	@Override
	public Long modifyFeed(Long memberId, FeedInfoDto feedInfoDto) {

		Optional<Feed> savedFeed = feedRepository.findById(feedInfoDto.getFeedId());
		if (savedFeed.get().getMemberId() == memberId) {
			savedFeed.get().setFeedTitle(feedInfoDto.getFeedTitle());
			savedFeed.get().setFeedContent(feedInfoDto.getFeedContent());
			savedFeed.get().setFeedUpdateAt(dateUtils.transDate(env.getProperty("dateutils.format")));

			return savedFeed.get().getFeedId();
		}

		//기존 tag 지우고 새로 저장
		List<FeedTag> feedTagList = feedTagRepository.findByFeed(savedFeed.get());
		for (FeedTag feedTag : feedTagList) {
			feedTagRepository.delete(feedTag);
		}
		for (String tag : feedInfoDto.getFeedTag()) {
			FeedTag feedTag = FeedTag.builder()
					.feed(savedFeed.get())
					.feedTagName(tag).build();
			feedTagRepository.save(feedTag);
		}

		//이미지 삭제
		if (feedInfoDto.getDeleteImg() != null) {
			for (Long deleteImgId : feedInfoDto.getDeleteImg()) {
				Optional<FeedImage> feedImage = feedImageRepository.findById(deleteImgId);

				List<FeedImageProduct> feedImageProductList = feedImageProductRepository.findByFeedImage(feedImage.get());
				for (FeedImageProduct feedImageProduct : feedImageProductList) {
					feedImageProductRepository.delete(feedImageProduct);
				}

				feedImageRepository.delete(feedImage.get());
			}
		}

		return savedFeed.get().getFeedId();
	}

	/**
	 * 피드 삭제하는 메서드
	 *
	 * @param userId
	 * @return FeedDetailResponse
	 */
	@Override
	public Long deleteFeed(Long userId, Long feedId) {

		Optional<Feed> savedFeed = feedRepository.findById(feedId);
		if (savedFeed.get().getMemberId() == userId) {
			savedFeed.get().setFeedStatus(false);
			return savedFeed.get().getFeedId();
		}

		return null;
	}

	/**
	 * 피드 조회수 증가하는 메서드
	 *
	 * @param feedId
	 * @return Boolean
	 */
	@Override
	public Boolean upViewCount(Long feedId) {
		Optional<Feed> savedFeed = feedRepository.findById(feedId);
		if (savedFeed.isPresent()) {
			savedFeed.get().setFeedViewCount(savedFeed.get().getFeedViewCount() + 1);
			return true;
		}
		return false;
	}

	/**
	 * 피드 공유수 증가하는 메서드
	 *
	 * @param feedId
	 * @return Boolean
	 */
	@Override
	public Boolean upShareCount(Long feedId) {
		Optional<Feed> savedFeed = feedRepository.findById(feedId);
		if (savedFeed.isPresent()) {
			savedFeed.get().setFeedShareCount(savedFeed.get().getFeedShareCount() + 1);
			return true;
		}
		return false;
	}

	/**
	 * 태그 별로 피드 조회하는 메서드
	 *
	 * @param feedTagName, pageNumber
	 * @return List<FeedResponse>
	 */
	@Override
	public FeedResponseResult findByFeedTag(String feedTagName, Integer pageNumber, Long loginMemberId) {
		List<FeedTag> feedTagList = feedTagRepository.findByFeedTagName(feedTagName);
		List<Feed> feedList = new ArrayList<>();
		for (FeedTag feedTag : feedTagList) {
			Optional<Feed> feed = feedRepository.findById(feedTag.getFeed().getFeedId());
			feedList.add(feed.get());
		}

		int startIndex = pageNumber * pageContentNumber;

		int size = feedList.size();

		FeedResponseResult responseResult = getResponses(size, startIndex, feedList, loginMemberId);
		responseResult.setCurrentPageNum(pageNumber);
		responseResult.setTotalElementNum(size);
		if(size%pageContentNumber==0){
			responseResult.setTotalPageNum(size/pageContentNumber);
		}
		else{
			responseResult.setTotalPageNum((size/pageContentNumber)+1);
		}
		return responseResult;
	}

	/**
	 * 피드 좋아요 취소 메서드
	 *
	 * @param feedId
	 * @param memberId
	 * @return
	 */
	@Override
	public Boolean deleteFeedLike(Long feedId, Long memberId) {

		Optional<Feed> feed = feedRepository.findById(feedId);
		Optional<FeedLike> feedLike = feedLikeRepository.findByFeedAndMemberId(feed.get(), memberId);
		if (!feedLike.isPresent()) { // 좋아요가 없는 경우 BAD REQUEST
			return Boolean.FALSE;
		}
		feed.get().setFeedLikeCount(feed.get().getFeedLikeCount() - 1);
		feedLikeRepository.delete(feedLike.get());
		return Boolean.TRUE;
	}

	/**
	 * 피드 좋아요 메서드
	 *
	 * @param feedId
	 * @param memberId
	 * @return
	 */
	@Override
	public Boolean createFeedLike(Long feedId, Long memberId) {

		Optional<Feed> feed = feedRepository.findById(feedId);
		Optional<FeedLike> feedLike = feedLikeRepository.findByFeedAndMemberId(feed.get(), memberId);
		if (!feedLike.isPresent()) { // 좋아요가 없는 경우만 좋아요 하게 함
			feed.get().setFeedLikeCount(feed.get().getFeedLikeCount() + 1);
			FeedLike newFeedLike = FeedLike.builder()
					.feed(feed.get())
					.memberId(memberId)
					.build();
			FeedLike result = feedLikeRepository.save(newFeedLike);
			if (result == null) {
				return Boolean.FALSE;
			}
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	@Override
	public Boolean createFeedScrap(Long feedId, Long memberId) {
		Optional<Feed> feed = feedRepository.findById(feedId);
		Optional<Scrap> scrap = scrapRepository.findByFeedAndMemberId(feed.get(), memberId);
		if (!scrap.isPresent()) { // 스크랩이 없는 경우만 좋아요 하게 함
			feed.get().setFeedScrapCount(feed.get().getFeedScrapCount() + 1);
			Scrap newScrap = Scrap.builder()
					.feed(feed.get())
					.memberId(memberId)
					.build();
			Scrap result = scrapRepository.save(newScrap);
			if (result == null) {
				return Boolean.FALSE;
			}
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	@Override
	public Boolean deleteFeedScrap(Long feedId, Long memberId) {
		Optional<Feed> feed = feedRepository.findById(feedId);
		Optional<Scrap> scrap = scrapRepository.findByFeedAndMemberId(feed.get(), memberId);
		if (!scrap.isPresent()) { // 스크랩이 없는 경우 BAD REQUEST
			return Boolean.FALSE;
		}
		feed.get().setFeedScrapCount(feed.get().getFeedScrapCount() - 1);
		scrapRepository.delete(scrap.get());
		return Boolean.TRUE;
	}

	/**
	 * feed 메인페이지 최신순 조회
	 *
	 * @param pageNumber
	 * @return
	 */
	@Override
	public FeedResponseResult findByRecentFeedList(Integer pageNumber, Long loginMemberId) {
		List<Feed> feedList = new ArrayList<>();
		feedRepository.findAll().forEach(feedList::add);
		feedList.sort((o1, o2) -> {
			int result = o2.getFeedCreateAt().compareTo(o1.getFeedCreateAt());
			return result;
		});

		int startIndex = pageNumber * pageContentNumber;

		int size = feedList.size();

		FeedResponseResult responseResult = getResponses(size, startIndex, feedList, loginMemberId);
		responseResult.setCurrentPageNum(pageNumber);
		responseResult.setTotalElementNum(size);
		if(size%pageContentNumber==0){
			responseResult.setTotalPageNum(size/pageContentNumber);
		}
		else{
			responseResult.setTotalPageNum((size/pageContentNumber)+1);
		}
		return responseResult;
	}

	/**
	 * feed 메인페이지 좋아요순 조회
	 *
	 * @param pageNumber
	 * @return
	 */
	@Override
	public FeedResponseResult findByLikeFeedList(Integer pageNumber, Long loginMemberId) {
		List<Feed> feedList = feedRepository.findAll(Sort.by(Sort.Direction.DESC, "feedLikeCount"));

		int startIndex = pageNumber * pageContentNumber;

		int size = feedList.size();

		FeedResponseResult responseResult = getResponses(size, startIndex, feedList, loginMemberId);
		responseResult.setCurrentPageNum(pageNumber);
		responseResult.setTotalElementNum(size);
		if(size%pageContentNumber==0){
			responseResult.setTotalPageNum(size/pageContentNumber);
		}
		else{
			responseResult.setTotalPageNum((size/pageContentNumber)+1);
		}
		return responseResult;
	}

	/**
	 * feed 메인페이지 팔로우 조회
	 *
	 * @param loginMemberId
	 * @param pageNumber
	 * @return
	 */
	@Override
	public FeedResponseResult findByFollowFeedList(Long loginMemberId, Integer pageNumber) {
		List<Feed> feedList = new ArrayList<>();

		List<Following> followers = followingRepository.findByFollowingMemberId(loginMemberId);

		for (Following follower : followers) {
			List<Feed> feedListByFollower = feedRepository.findByMemberId(follower.getFollowerMemberId());
			for(Feed feed : feedListByFollower) {
				feedList.add(feed);
			}
		}

		int startIndex = pageNumber * pageContentNumber;

		int size = feedList.size();

		FeedResponseResult responseResult = getResponses(size, startIndex, feedList, loginMemberId);
		responseResult.setCurrentPageNum(pageNumber);
		responseResult.setTotalElementNum(size);
		if(size%pageContentNumber==0){
			responseResult.setTotalPageNum(size/pageContentNumber);
		}
		else{
			responseResult.setTotalPageNum((size/pageContentNumber)+1);
		}
		return responseResult;
	}

	/**
	 * feed 메인페이지 조회수순 조회
	 *
	 * @param pageNumber
	 * @return
	 */
	@Override
	public FeedResponseResult findByViewCountFeedList(Integer pageNumber, Long loginMemberId) {
		List<Feed> feedList = feedRepository.findAll(Sort.by(Sort.Direction.DESC, "feedViewCount"));

		int startIndex = pageNumber * pageContentNumber;

		int size = feedList.size();

		FeedResponseResult responseResult = getResponses(size, startIndex, feedList, loginMemberId);
		responseResult.setCurrentPageNum(pageNumber);
		responseResult.setTotalElementNum(size);
		if(size%pageContentNumber==0){
			responseResult.setTotalPageNum(size/pageContentNumber);
		}
		else{
			responseResult.setTotalPageNum((size/pageContentNumber)+1);
		}
		return responseResult;
	}

	/**
	 * 조회 목록을 페이징처리 해주는 메서드
	 *
	 * @param size
	 * @param startIndex
	 * @param feedList
	 * @param loginMemberId
	 * @return
	 */
	public FeedResponseResult getResponses(int size, int startIndex, List<Feed> feedList, Long loginMemberId) {
		FeedResponseResult responseResult = new FeedResponseResult();
		List<FeedResponse> responseList = new ArrayList<>();

		Map<Long,Integer> followMap = new HashMap<>();
		List<Following> followings = followingRepository.findByFollowingMemberId(loginMemberId);
		for(Following following : followings){
			followMap.put(following.getFollowerMemberId(), 1);
		}

		if(size < startIndex){
			responseResult.setFeedResponseList(responseList);
			return responseResult;
		}
		if (size < startIndex + pageContentNumber) {
			for (Feed feed : feedList.subList(startIndex, size)) {
				if (feed != null) {
					FeedResponse response = FeedResponse.builder()
							.feedTitle(feed.getFeedTitle())
							.feedId(feed.getFeedId())
							.feedCommentCount(feed.getFeedCommentCount())
							.feedLikeCount(feed.getFeedLikeCount())
							.feedScrapCount(feed.getFeedScrapCount())
							.feedShareCount(feed.getFeedShareCount())
							.feedViewCount(feed.getFeedViewCount())
							.memberId(feed.getMemberId())
							.memberRole(feed.getMemberRole())
							.feedContent(feed.getFeedContent())
							.isModifiable(false)
							.feedLikeStatus(false)
							.scrapStatus(false)
							.followStatus(false)
							.build();

					// feed 작성자와 로그인한 사용자가 같은지 여부
					if (feed.getMemberId() == loginMemberId) {
						response.setIsModifiable(true);
					}

					// follow 한 상태인지 여부
					if(followMap.containsKey(feed.getMemberId())){
						response.setFollowStatus(true);
					}

					// feed에 대한 스크랩, 좋아요 여부
					Optional<FeedLike> feedLike = feedLikeRepository.findByFeedAndMemberId(feed, loginMemberId);
					if (feedLike.isPresent()) {
						response.setFeedLikeStatus(true);
					}
					Optional<Scrap> scrap = scrapRepository.findByFeedAndMemberId(feed, loginMemberId);
					if (scrap.isPresent()) {
						response.setScrapStatus(true);
					}

					if (feed.getMemberRole().equals("user")) { // 유저
						Optional<UserVo> user = userRepository.findById(feed.getMemberId());
						response.setMemberName(user.get().getUserName());
						response.setMemberProfileImg(user.get().getUserProfileImg());
					} else if (feed.getMemberRole().equals("seller")) { // 셀러
						Optional<SellerVo> seller = sellerRepository.findById(feed.getMemberId());
						response.setMemberName(seller.get().getSellerName());
						response.setMemberProfileImg(seller.get().getSellerProfileImg());
					}
					List<FeedImage> feedImage = feedImageRepository.findByFeed(feed);
					response.setFeedImageSrc(feedImage.get(0).getFeedImageSrc());

					responseList.add(response);
				}
			}
			responseResult.setFeedResponseList(responseList);
			return responseResult;
		}
		for (Feed feed : feedList.subList(startIndex, startIndex + pageContentNumber)) {
			FeedResponse response = FeedResponse.builder()
					.feedTitle(feed.getFeedTitle())
					.feedId(feed.getFeedId())
					.feedCommentCount(feed.getFeedCommentCount())
					.feedLikeCount(feed.getFeedLikeCount())
					.feedScrapCount(feed.getFeedScrapCount())
					.feedShareCount(feed.getFeedShareCount())
					.feedViewCount(feed.getFeedViewCount())
					.memberId(feed.getMemberId())
					.memberRole(feed.getMemberRole())
					.feedContent(feed.getFeedContent())
					.isModifiable(false)
					.feedLikeStatus(false)
					.scrapStatus(false)
					.followStatus(false)
					.build();

			// feed 작성자와 로그인한 사용자가 같은지 여부
			if (feed.getMemberId() == loginMemberId) {
				response.setIsModifiable(true);
			}

			// follow 한 상태인지 여부
			if(followMap.containsKey(feed.getMemberId())){
				response.setFollowStatus(true);
			}

			// feed에 대한 스크랩, 좋아요 여부
			Optional<FeedLike> feedLike = feedLikeRepository.findByFeedAndMemberId(feed, loginMemberId);
			if (feedLike.isPresent()) {
				response.setFeedLikeStatus(true);
			}
			Optional<Scrap> scrap = scrapRepository.findByFeedAndMemberId(feed, loginMemberId);
			if (scrap.isPresent()) {
				response.setScrapStatus(true);
			}

			if (feed.getMemberRole().equals("user")) { // 유저
				Optional<UserVo> user = userRepository.findById(feed.getMemberId());
				response.setMemberName(user.get().getUserName());
				response.setMemberProfileImg(user.get().getUserProfileImg());
			} else if (feed.getMemberRole().equals("seller")) { // 셀러
				Optional<SellerVo> seller = sellerRepository.findById(feed.getMemberId());
				response.setMemberName(seller.get().getSellerName());
				response.setMemberProfileImg(seller.get().getSellerProfileImg());
			}
			List<FeedImage> feedImage = feedImageRepository.findByFeed(feed);
			response.setFeedImageSrc(feedImage.get(0).getFeedImageSrc());
			responseList.add(response);
		}
		responseResult.setFeedResponseList(responseList);
		return responseResult;
	}

	/**
	 * 조회 목록에 대한 리스트를 만드는 메서드
	 * @param wishList
	 * @Param memberId
	 * @return List<FeedResponse>
	 */
	public WishProductListResult getResponseForWish(int size, int startIndex, List<Wish> wishList){
		WishProductListResult wishProductListResult = new WishProductListResult();
		List<WishProductListResponse> responseList = new ArrayList<>();
		if(size < startIndex){
			wishProductListResult.setWishProductListResponse(responseList);
			return wishProductListResult;
		}

		if(size < startIndex + pageContentNumber) {
			for (Wish wish : wishList.subList(startIndex, size)) {
				if (wish != null) {
					List<Review> reviews = reviewRepository.findReviewByProduct(wish.getProduct());
					Long sumOfReviewCount = 0L;
					int reviewCount = 0;
					Long reviewRate = 0L;
					if(reviews.size() != 0) {
						for (Review review : reviews) {
							sumOfReviewCount += review.getReviewRate();
						}
						reviewCount = reviews.size();
						reviewRate = Math.round((sumOfReviewCount / reviews.size()) * 100 / 100.0);
					} else {
						reviewRate = 0L;
						reviewCount = 0;
					}


					WishProductListResponse wishProductListResponse = WishProductListResponse.builder()
							.productId(wish.getProduct().getProductId())
							.productName(wish.getProduct().getProductName())
							.productPrice(wish.getProduct().getProductPrice())
							.productMainImgSrc(wish.getProduct().getProductMainImgSrc())
							.productOriginPlace(wish.getProduct().getProductOriginPlace())
							.productWishCount(wish.getProduct().getProductWishCount())
							.productStatus(wish.getProduct().getProductStatus())
							.sellerName(wish.getProduct().getSeller().getSellerName())
							.reviewRate(reviewRate)
							.reviewCount(reviewCount)
							.build();

					responseList.add(wishProductListResponse);
				}
			}
			wishProductListResult.setWishProductListResponse(responseList);
			return wishProductListResult;
		}

		for (Wish wish : wishList.subList(startIndex, startIndex + pageContentNumber)) {
			if (wish != null) {
				List<Review> reviews = reviewRepository.findReviewByProduct(wish.getProduct());
				Long sumOfReviewCount = 0L;
				int reviewCount = 0;
				Long reviewRate = 0L;
				if(reviews.size() != 0) {
					for (Review review : reviews) {
						sumOfReviewCount += review.getReviewRate();
					}
					reviewCount = reviews.size();
					reviewRate = Math.round((sumOfReviewCount / reviews.size()) * 100 / 100.0);
				} else {
					reviewRate = 0L;
					reviewCount = 0;
				}
				WishProductListResponse wishProductListResponse = WishProductListResponse.builder()
						.productId(wish.getProduct().getProductId())
						.productName(wish.getProduct().getProductName())
						.productPrice(wish.getProduct().getProductPrice())
						.productMainImgSrc(wish.getProduct().getProductMainImgSrc())
						.productOriginPlace(wish.getProduct().getProductOriginPlace())
						.productWishCount(wish.getProduct().getProductWishCount())
						.productStatus(wish.getProduct().getProductStatus())
						.sellerName(wish.getProduct().getSeller().getSellerName())
						.reviewRate(reviewRate)
						.reviewCount(reviewCount)
						.build();

				responseList.add(wishProductListResponse);
			}
		}

		wishProductListResult.setWishProductListResponse(responseList);
		return wishProductListResult;
	}

	@Override
	public List<ProfileMainFeedResponse> findFeedListByMember(ProfileMainFeedDto profileMainFeedDto) {
		PageRequest pageRequest = PageRequest.of(0, 8, Sort.by("feedCreateAt").descending());
		Long memberId = profileMainFeedDto.getMemberId();

		Page<Feed> feedList = feedRepository.findMainFeedListByMemberId(pageRequest, memberId);
		List<ProfileMainFeedResponse> responseList = new ArrayList<>();
		for (Feed feed : feedList) {
			FeedImage feedThumbnailImage = feedImageRepository.findByFeed(feed).get(0);
			ProfileMainFeedResponse profileMainFeedResponse = ProfileMainFeedResponse.builder()
					.feedId(feed.getFeedId())
					.feedImageId(feedThumbnailImage.getFeedImageId())
					.feedImageSrc(feedThumbnailImage.getFeedImageSrc())
					.build();
			responseList.add(profileMainFeedResponse);
		}
		return responseList;
	}

	@Override
	public List<ProfileMainScrapResponse> findByMemberScrapList(ProfileMainScrapDto profileMainScrapDto) {
		PageRequest pageRequest = PageRequest.of(0, 8, Sort.by("scrapId").descending());
		Long memberId = profileMainScrapDto.getMemberId();
		List<ProfileMainScrapResponse> responseList = new ArrayList<>();
		Page<Scrap> scrapList = scrapRepository.findMainScrapListByMemberId(pageRequest, memberId);
		for (Scrap scrap : scrapList) {
			FeedImage feedImage = feedImageRepository.findByFeed(scrap.getFeed()).get(0);
			ProfileMainScrapResponse profileMainScrapResponse = ProfileMainScrapResponse.builder()
					.feedId(scrap.getFeed().getFeedId())
					.feedImageId(feedImage.getFeedImageId())
					.feedImageSrc(feedImage.getFeedImageSrc())
					.build();

			Optional<Feed> savedFeed = feedRepository.findById(scrap.getFeed().getFeedId());
			if(savedFeed.get().getMemberRole().equals("user")) {
				UserVo user = memberServiceClient.getUserByUserId(savedFeed.get().getMemberId());
				profileMainScrapResponse.setMemberName(user.getUserName());
			}
			else{
				SellerVo seller = memberServiceClient.getSellerBySellerId(savedFeed.get().getMemberId());
				profileMainScrapResponse.setMemberName(seller.getSellerName());
			}
			responseList.add(profileMainScrapResponse);
		}
		return responseList;
	}

	@Override
	public List<ProfileMainWishResponse> findWishListByMember(ProfileMainWishDto profileMainWishDto) {
		PageRequest pageRequest = PageRequest.of(0, 8, Sort.by("wishId").descending());
		Long memberId = profileMainWishDto.getMemberId();
		List<ProfileMainWishResponse> responseList = new ArrayList<>();

		List<WishVo> wishList = productServiceClient.findWishListByMemberId(pageRequest, memberId);
		for(WishVo wish : wishList){
			ProductVo product = productServiceClient.findProductByProductId(wish.getProductId());
			ProfileMainWishResponse profileMainWishResponse = ProfileMainWishResponse.builder()
					.productId(product.getProductId())
					.productImgSrc(product.getProductMainImgSrc())
					.build();
			responseList.add(profileMainWishResponse);
		}

		return responseList;
	}

	@Override
	public FeedResponseResult findByRecentFeedListAndMemberId(ProfileFeedDto profileFeedDto) {

		List<Feed> feedPageList = feedRepository.findFeedListByMemberId(profileFeedDto.getMemberId());
		List<Feed> feedList = new ArrayList<>();
		for(Feed feed : feedPageList){
			feedList.add(feed);
		}

		int startIndex = profileFeedDto.getPageNumber() * pageContentNumber;
		int size = feedList.size();

		FeedResponseResult responseResult = getResponses(size, startIndex, feedList, profileFeedDto.getLoginMemberId());
		responseResult.setCurrentPageNum(profileFeedDto.getPageNumber());
		responseResult.setTotalElementNum(size);
		if(size%pageContentNumber==0){
			responseResult.setTotalPageNum(size/pageContentNumber);
		}
		else{
			responseResult.setTotalPageNum((size/pageContentNumber)+1);
		}
		return responseResult;
	}

	@Override
	public FeedResponseResult findByRecentScrapFeedListAndMemberId(ProfileFeedDto profileFeedDto) {

		List<Scrap> scrapList = scrapRepository.findScrapListByMemberId(profileFeedDto.getMemberId());
		List<Feed> feedList = new ArrayList<>();
		for (Scrap scrap : scrapList) {
			feedList.add(scrap.getFeed());
		}

		int startIndex = profileFeedDto.getPageNumber() * pageContentNumber;
		int size = feedList.size();

		FeedResponseResult responseResult = getResponses(size, startIndex, feedList, profileFeedDto.getLoginMemberId());
		responseResult.setCurrentPageNum(profileFeedDto.getPageNumber());
		responseResult.setTotalElementNum(size);
		if(size%pageContentNumber==0){
			responseResult.setTotalPageNum(size/pageContentNumber);
		}
		else{
			responseResult.setTotalPageNum((size/pageContentNumber)+1);
		}
		return responseResult;
	}

	@Override
	public WishProductListResult findByMemberWishDetailList(ProfileMainWishDto profileMainWishDto) {

		Long memberId = profileMainWishDto.getMemberId();

		List<Wish> wishList = productWishRepository.findWishListByUserId(memberId);

		int startIndex = profileMainWishDto.getPageNumber() * pageContentNumber;
		int size = wishList.size();

		WishProductListResult wishProductListResult = getResponseForWish(size, startIndex, wishList);
		wishProductListResult.setCurrentPageNum(profileMainWishDto.getPageNumber());
		wishProductListResult.setTotalElementNum(size);
		if(size%pageContentNumber==0){
			wishProductListResult.setTotalPageNum(size/pageContentNumber);
		}
		else{
			wishProductListResult.setTotalPageNum((size/pageContentNumber)+1);
		}

		return wishProductListResult;
	}

	@Override
	public MemberProfileCountResponse getFeedScrapWishCount(MemberProfileDto memberProfileDto) {

		List<Feed> feedList = feedRepository.findFeedListByMemberId(memberProfileDto.getMemberId());
		List<Scrap> scrapList = scrapRepository.findScrapListByMemberId(memberProfileDto.getMemberId());
		List<WishVo> wishList = productWishRepository.findWishListByUserId(memberProfileDto.getMemberId());

		MemberProfileCountResponse memberProfileCountResponse = MemberProfileCountResponse.builder()
				.photoCount(feedList.size())
				.scrapCount(scrapList.size())
				.wishCount(wishList.size())
				.build();

		return memberProfileCountResponse;
	}


}
