package com.team6.onandthefarmsnsservice.service;

import com.team6.onandthefarmsnsservice.dto.FeedDto;
import com.team6.onandthefarmsnsservice.dto.FeedInfoDto;
import com.team6.onandthefarmsnsservice.entity.Feed;
import com.team6.onandthefarmsnsservice.entity.FeedImage;
import com.team6.onandthefarmsnsservice.entity.FeedImageProduct;
import com.team6.onandthefarmsnsservice.feignclient.MemberServiceClient;
import com.team6.onandthefarmsnsservice.repository.FeedImageProductRepository;
import com.team6.onandthefarmsnsservice.repository.FeedImageRepository;
import com.team6.onandthefarmsnsservice.repository.FeedRepository;
import com.team6.onandthefarmsnsservice.utils.DateUtils;
import com.team6.onandthefarmsnsservice.vo.FeedResponse;
import com.team6.onandthefarmsnsservice.vo.ImageProduct;
import com.team6.onandthefarmsnsservice.vo.user.Seller;
import com.team6.onandthefarmsnsservice.vo.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class FeedServiceImpl implements FeedService{

    private final int pageContentNumber = 8;

    private FeedRepository feedRepository;

    private MemberServiceClient memberServiceClient;

    private FeedImageRepository feedImageRepository;

    private FeedImageProductRepository feedImageProductRepository;

    private DateUtils dateUtils;
    Environment env;

    @Autowired
    public FeedServiceImpl(FeedRepository feedRepository,
                           MemberServiceClient memberServiceClient,
                           FeedImageRepository feedImageRepository,
                           FeedImageProductRepository feedImageProductRepository,
                           DateUtils dateUtils,
                           Environment env) {
        this.feedRepository = feedRepository;
        this.memberServiceClient=memberServiceClient;
        this.feedImageRepository=feedImageRepository;
        this.feedImageProductRepository = feedImageProductRepository;
        this.dateUtils = dateUtils;
        this.env = env;
    }

    public List<FeedResponse> findByRecentFeedList(FeedDto feedDto){
        List<Feed> feedList = new ArrayList<>();
        feedRepository.findAll().forEach(feedList::add);
        feedList.sort((o1, o2) -> {
            int result = o2.getFeedCreateAt().compareTo(o1.getFeedCreateAt());
            return result;
        });

        int startIndex = feedDto.getPageNumber()*pageContentNumber;

        int size = feedList.size();

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
                        .memberRoll(feed.getMemberRoll())
                        .build();

                User user = null;
                Seller seller = null;

                if(feed.getMemberRoll()==1){ // 유저
                    user = memberServiceClient.findByUserId(feed.getMemberId());
                    response.setMemberName(user.getUserName());
                }
                else if(feed.getMemberRoll()==2){ // 셀러
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
                    .memberRoll(feed.getMemberRoll())
                    .build();

            User user = null;
            Seller seller = null;

            if(feed.getMemberRoll()==1){ // 유저
                user = memberServiceClient.findByUserId(feed.getMemberId());
                response.setMemberName(user.getUserName());
            }
            else if(feed.getMemberRoll()==2){ // 셀러
                seller = memberServiceClient.findBySellerId(feed.getMemberId());
                response.setMemberName(seller.getSellerName());
            }
            List<FeedImage> feedImage = feedImageRepository.findByFeed(feed);
            response.setFeedImageSrc(feedImage.get(0).getFeedImageSrc());
            responseList.add(response);
        }
        return responseList;
    }

    @Override
    public Long uploadFeed(Long memberId, String memberRole, FeedInfoDto feedInfoDto) {

        Feed feed = new Feed();
        feed.setMemberId(memberId);
        feed.setMemberRoll(memberRole);
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
