package com.team6.onandthefarmsnsservice.service;

import com.team6.onandthefarmsnsservice.dto.FeedDto;
import com.team6.onandthefarmsnsservice.dto.SnsFeedDto;
import com.team6.onandthefarmsnsservice.entity.Feed;
import com.team6.onandthefarmsnsservice.entity.FeedImage;
import com.team6.onandthefarmsnsservice.feignclient.MemberServiceClient;
import com.team6.onandthefarmsnsservice.repository.FeedImageRepository;
import com.team6.onandthefarmsnsservice.repository.FeedRepository;
import com.team6.onandthefarmsnsservice.vo.FeedResponse;
import com.team6.onandthefarmsnsservice.vo.user.Seller;
import com.team6.onandthefarmsnsservice.vo.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FeedServiceImp implements FeedService{

    private final int pageContentNumber = 8;

    private FeedRepository feedRepository;

    private MemberServiceClient memberServiceClient;

    private FeedImageRepository feedImageRepository;

    @Autowired
    public FeedServiceImp(FeedRepository feedRepository,
                          MemberServiceClient memberServiceClient,
                          FeedImageRepository feedImageRepository) {
        this.feedRepository = feedRepository;
        this.memberServiceClient=memberServiceClient;
        this.feedImageRepository=feedImageRepository;
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

}
