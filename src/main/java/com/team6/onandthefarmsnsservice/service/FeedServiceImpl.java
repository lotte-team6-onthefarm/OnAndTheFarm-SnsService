package com.team6.onandthefarmsnsservice.service;

import com.team6.onandthefarmsnsservice.dto.FeedDto;
import com.team6.onandthefarmsnsservice.dto.FeedInfoDto;
import com.team6.onandthefarmsnsservice.entity.Feed;
import com.team6.onandthefarmsnsservice.entity.FeedImage;
import com.team6.onandthefarmsnsservice.entity.FeedLike;
import com.team6.onandthefarmsnsservice.entity.Scrap;
import com.team6.onandthefarmsnsservice.entity.FeedImageProduct;
import com.team6.onandthefarmsnsservice.feignclient.MemberServiceClient;
import com.team6.onandthefarmsnsservice.repository.FeedImageProductRepository;
import com.team6.onandthefarmsnsservice.repository.FeedImageRepository;
import com.team6.onandthefarmsnsservice.repository.FeedLikeRepository;
import com.team6.onandthefarmsnsservice.repository.FeedRepository;
import com.team6.onandthefarmsnsservice.repository.ScrapRepository;
import com.team6.onandthefarmsnsservice.vo.FeedResponse;
import com.team6.onandthefarmsnsservice.vo.user.Following;
import com.team6.onandthefarmsnsservice.vo.user.Seller;
import com.team6.onandthefarmsnsservice.vo.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import com.team6.onandthefarmsnsservice.utils.DateUtils;
import com.team6.onandthefarmsnsservice.vo.ImageProduct;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FeedServiceImpl implements FeedService{

    private final int pageContentNumber = 8;

    private final FeedRepository feedRepository;

    private final MemberServiceClient memberServiceClient;

    private final FeedImageRepository feedImageRepository;

    private final FeedLikeRepository feedLikeRepository;

    private final ScrapRepository scrapRepository;
    private final FeedImageProductRepository feedImageProductRepository;

    private DateUtils dateUtils;
    Environment env;

    @Autowired
    public FeedServiceImpl(FeedRepository feedRepository,
                           MemberServiceClient memberServiceClient,
                           FeedImageRepository feedImageRepository,
                           FeedLikeRepository feedLikeRepository,
                           ScrapRepository scrapRepository,
                           FeedImageProductRepository feedImageProductRepository,
                           DateUtils dateUtils,
                           Environment env) {

            this.feedLikeRepository=feedLikeRepository;
            this.scrapRepository=scrapRepository;
            this.feedRepository = feedRepository;
            this.memberServiceClient=memberServiceClient;
            this.feedImageRepository=feedImageRepository;
            this.feedImageProductRepository = feedImageProductRepository;
            this.dateUtils = dateUtils;
            this.env = env;
    }

    /**
     * feed 메인페이지 최신순 조회
     * @param feedDto
     * @return
     */
    public List<FeedResponse> findByRecentFeedList(FeedDto feedDto){
        List<Feed> feedList = new ArrayList<>();
        feedRepository.findAll().forEach(feedList::add);
        feedList.sort((o1, o2) -> {
            int result = o2.getFeedCreateAt().compareTo(o1.getFeedCreateAt());
            return result;
        });

        int startIndex = feedDto.getPageNumber()*pageContentNumber;

        int size = feedList.size();

        return getResponses(size,startIndex,feedList);
    }

    /**
     * feed 메인페이지 좋아요순 조회
     * @param pageNumber
     * @return
     */
    public List<FeedResponse> findByLikeFeedList(Integer pageNumber){
        List<Feed> feedList = feedRepository.findAll(Sort.by(Sort.Direction.DESC, "feedLikeCount"));

        int startIndex = pageNumber*pageContentNumber;

        int size = feedList.size();

        return getResponses(size,startIndex,feedList);
    }

    /**
     * feed 메인페이지 팔로우 조회
     * @param memberId
     * @param pageNumber
     * @return
     */
    public List<FeedResponse> findByFollowFeedList(Long memberId,Integer pageNumber){
        List<Feed> feedList = new ArrayList<>();

        List<Following> followers = memberServiceClient.findByFollowingMemberId(memberId);

        for(Following follower : followers){
            Feed feed = feedRepository.findByMemberId(follower.getFollowerMemberId());
            feedList.add(feed);
        }

        int startIndex = pageNumber*pageContentNumber;

        int size = feedList.size();

        return getResponses(size,startIndex,feedList);
    }

    /**
     * feed 메인페이지 조회수순 조회
     * @param pageNumber
     * @return
     */
    public List<FeedResponse> findByViewCountFeedList(Integer pageNumber){
        List<Feed> feedList = feedRepository.findAll(Sort.by(Sort.Direction.DESC, "feedViewCount"));

        int startIndex = pageNumber*pageContentNumber;

        int size = feedList.size();

        return getResponses(size,startIndex,feedList);
    }

    /**
     * 조회 목록을 페이징처리 해주는 메서드
     * @param size
     * @param startIndex
     * @param feedList
     * @return
     */
    public List<FeedResponse> getResponses(int size, int startIndex, List<Feed> feedList){
        List<FeedResponse> responseList = new ArrayList<>();
        if(size<startIndex+pageContentNumber){
            for(Feed feed : feedList.subList(startIndex,size)){

                FeedResponse response = FeedResponse.builder()
                        .feedTitle(feed.getFeedTitle())
                        .feedId(feed.getFeedId())
                        .feedCommentCount(feed.getFeedCommentCount())
                        .feedLikeCount(feed.getFeedLikeCount())
                        .feedScrapCount(feed.getFeedScrapCount())
                        .feedShareCount(feed.getFeedShareCount())
                        .feedViewCount(feed.getFeedViewCount())
                        .memberId(feed.getMemberId())
                        .memberRole(Integer.valueOf(feed.getMemberRole()))
                        .build();

                User user = null;
                Seller seller = null;

                if(feed.getMemberRole().equals("user")){ // 유저
                    user = memberServiceClient.findByUserId(feed.getMemberId());
                    response.setMemberName(user.getUserName());
                }
                else if(feed.getMemberRole().equals("seller")){ // 셀러
                    seller = memberServiceClient.findBySellerId(feed.getMemberId());
                    response.setMemberName(seller.getSellerName());
                }
                List<FeedImage> feedImage = feedImageRepository.findByFeed(feed);
                response.setFeedImageSrc(feedImage.get(0).getFeedImageSrc());
                responseList.add(response);
            }
            return responseList;
        }
        for(Feed feed : feedList.subList(startIndex,startIndex+pageContentNumber)){
            FeedResponse response = FeedResponse.builder()
                    .feedTitle(feed.getFeedTitle())
                    .feedId(feed.getFeedId())
                    .feedCommentCount(feed.getFeedCommentCount())
                    .feedLikeCount(feed.getFeedLikeCount())
                    .feedScrapCount(feed.getFeedScrapCount())
                    .feedShareCount(feed.getFeedShareCount())
                    .feedViewCount(feed.getFeedViewCount())
                    .memberId(feed.getMemberId())
                    .memberRole(Integer.valueOf(feed.getMemberRole()))
                    .build();

            User user = null;
            Seller seller = null;

            if(feed.getMemberRole().equals("user")){ // 유저
                user = memberServiceClient.findByUserId(feed.getMemberId());
                response.setMemberName(user.getUserName());
            }
            else if(feed.getMemberRole().equals("seller")){ // 셀러
                seller = memberServiceClient.findBySellerId(feed.getMemberId());
                response.setMemberName(seller.getSellerName());
            }
            List<FeedImage> feedImage = feedImageRepository.findByFeed(feed);
            response.setFeedImageSrc(feedImage.get(0).getFeedImageSrc());
            responseList.add(response);
        }
        return responseList;
    }

    /**
     * 피드 좋아요 메서드
     * @param feedId
     * @param userId
     * @return
     */
    public Boolean createFeedLike(Long feedId, Long userId){
        Optional<Feed> feed = feedRepository.findById(feedId);
        feed.get().setFeedLikeCount(feed.get().getFeedLikeCount()+1);
        FeedLike feedLike = FeedLike.builder()
                .feed(feed.get())
                .memberId(userId)
                .build();
        FeedLike result = feedLikeRepository.save(feedLike);
        if(result==null){
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public Boolean createFeedScrap(Long feedId, Long userId){
        Optional<Feed> feed = feedRepository.findById(feedId);
        feed.get().setFeedScrapCount(feed.get().getFeedScrapCount()+1);
        Scrap scrap = Scrap.builder()
                .feed(feed.get())
                .memberId(userId)
                .build();
        Scrap result = scrapRepository.save(scrap);
        if(result==null){
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }


    /**
     * 피드 업로드하는 메서드
     * @param memberId, memberRole, feedInfoDto
     * @return feedId
     */
    @Override
    public Long uploadFeed(Long memberId, String memberRole, FeedInfoDto feedInfoDto) {

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

        Feed savedFeed = feedRepository.save(feed);

        int imageIndex = 0;
        for (MultipartFile imageSrc : feedInfoDto.getFeedImgSrcList()){
            //imageSrc 처리 코드 필요
            String imageOriginName = imageSrc.getOriginalFilename();

            FeedImage feedImage = new FeedImage();
            feedImage.setFeed(savedFeed);
            feedImage.setFeedImageSrc(imageOriginName);

            FeedImage saveFeedImage = feedImageRepository.save(feedImage);

            for(ImageProduct imageProduct : feedInfoDto.getFeedProductIdList()){
                if(imageProduct.getImageNumber() == imageIndex){
                    FeedImageProduct feedImageProduct = new FeedImageProduct();
                    feedImageProduct.setFeedImage(saveFeedImage);
                    feedImageProduct.setProductId(imageProduct.getProductId());

                    FeedImageProduct savedFeedImageProduct = feedImageProductRepository.save(feedImageProduct);
                }
            }
            imageIndex++;
        }

        return savedFeed.getFeedId();
    }

}
