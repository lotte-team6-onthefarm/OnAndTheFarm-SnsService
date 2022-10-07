package com.team6.onandthefarmsnsservice.service;

import com.team6.onandthefarmsnsservice.dto.comment.CommentInfoDto;
import com.team6.onandthefarmsnsservice.entity.Feed;
import com.team6.onandthefarmsnsservice.entity.FeedComment;
import com.team6.onandthefarmsnsservice.feignclient.MemberServiceClient;
import com.team6.onandthefarmsnsservice.repository.FeedCommentRepository;
import com.team6.onandthefarmsnsservice.repository.FeedRepository;
import com.team6.onandthefarmsnsservice.utils.DateUtils;
import com.team6.onandthefarmsnsservice.vo.comment.CommentDetailResponse;
import com.team6.onandthefarmsnsservice.vo.user.Seller;
import com.team6.onandthefarmsnsservice.vo.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private final FeedRepository feedRepository;
    private final FeedCommentRepository feedCommentRepository;
    private final MemberServiceClient memberServiceClient;
    private DateUtils dateUtils;
    Environment env;

    @Autowired
    public CommentServiceImpl(FeedRepository feedRepository,
                              FeedCommentRepository feedCommentRepository,
                              MemberServiceClient memberServiceClient,
                              DateUtils dateUtils,
                              Environment env){
        this.feedRepository = feedRepository;
        this.feedCommentRepository = feedCommentRepository;
        this.memberServiceClient = memberServiceClient;
        this.dateUtils = dateUtils;
        this.env = env;
    }

    @Override
    public List<CommentDetailResponse> findCommentDetail(Long feedId) {

        List<CommentDetailResponse> commentDetailList = new ArrayList<>();

        List<FeedComment> feedCommentList = feedCommentRepository.findByFeedId(feedId);
        for(FeedComment feedComment : feedCommentList){
            CommentDetailResponse commentDetail = CommentDetailResponse.builder()
                    .memberId(feedComment.getMemberId())
                    .memberRole(feedComment.getMemberRole())
                    .feedCommnetId(feedComment.getFeedCommnetId())
                    .feedCommentContent(feedComment.getFeedCommentContent())
                    .feedCommentCreateAt(feedComment.getFeedCommentCreateAt())
                    .feedCommentModifiedAt(feedComment.getFeedCommentModifiedAt())
                    .build();

            if(feedComment.getMemberRole().equals("user")){
                User user = memberServiceClient.findByUserId(feedComment.getMemberId());
                commentDetail.setMemberName(user.getUserName());
            }
            else if(feedComment.getMemberRole().equals("seller")){
                Seller seller = memberServiceClient.findBySellerId(feedComment.getMemberId());
                commentDetail.setMemberName(seller.getSellerName());
            }

            commentDetailList.add(commentDetail);
        }

        return commentDetailList;
    }

    @Override
    public Long addComment(CommentInfoDto commentInfoDto) {

        Optional<Feed> feed = feedRepository.findById(commentInfoDto.getFeedId());

        FeedComment feedComment = new FeedComment();
        feedComment.setMemberId(commentInfoDto.getMemberId());
        feedComment.setMemberRole(commentInfoDto.getMemberRole());
        feedComment.setFeed(feed.get());
        feedComment.setFeedCommentContent(commentInfoDto.getFeedCommentContent());
        feedComment.setFeedCommentCreateAt(dateUtils.transDate(env.getProperty("dateutils.format")));

        FeedComment savedFeedComment = feedCommentRepository.save(feedComment);
        return savedFeedComment.getFeedCommnetId();
    }

    @Override
    public Long modifyComment(CommentInfoDto commentInfoDto) {

        Optional<FeedComment> feedComment = feedCommentRepository.findById(commentInfoDto.getFeedCommentId());

        if(feedComment.isPresent()){
            feedComment.get().setFeedCommentContent(commentInfoDto.getFeedCommentContent());
            feedComment.get().setFeedCommentModifiedAt(dateUtils.transDate(env.getProperty("dateutils.format")));
        }

        return feedComment.get().getFeedCommnetId();
    }
}
