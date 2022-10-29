package com.team6.onandthefarmsnsservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.team6.onandthefarmsnsservice.feignclient.MemberServiceClient;
import com.team6.onandthefarmsnsservice.vo.user.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team6.onandthefarmsnsservice.dto.comment.CommentInfoDto;
import com.team6.onandthefarmsnsservice.entity.Feed;
import com.team6.onandthefarmsnsservice.entity.FeedComment;
import com.team6.onandthefarmsnsservice.repository.FeedCommentRepository;
import com.team6.onandthefarmsnsservice.repository.FeedRepository;
import com.team6.onandthefarmsnsservice.utils.DateUtils;
import com.team6.onandthefarmsnsservice.vo.comment.CommentDetailResponse;
import com.team6.onandthefarmsnsservice.vo.user.SellerVo;

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
    public List<CommentDetailResponse> findCommentDetail(Long feedId, Long memberId) {

        List<CommentDetailResponse> commentDetailList = new ArrayList<>();

        List<FeedComment> feedCommentList = feedCommentRepository.findByFeedId(feedId);
        for(FeedComment feedComment : feedCommentList){

            CommentDetailResponse commentDetail = CommentDetailResponse.builder()
                    .memberId(feedComment.getMemberId())
                    .memberRole(feedComment.getMemberRole())
                    .feedCommentId(feedComment.getFeedCommnetId())
                    .feedCommentContent(feedComment.getFeedCommentContent())
                    .feedCommentCreateAt(feedComment.getFeedCommentCreateAt())
                    .feedCommentModifiedAt(feedComment.getFeedCommentModifiedAt())
                    .isModifiable(false)
                    .build();

            if(feedComment.getMemberId().equals(memberId)){
                commentDetail.setIsModifiable(true);
            }

            if(feedComment.getMemberRole().equals("user")){
                UserVo userVo = memberServiceClient.findByUserId(feedComment.getMemberId());
                commentDetail.setMemberName(userVo.getUserName());
                commentDetail.setMemberProfileImg(userVo.getUserProfileImg());
            }
            else if(feedComment.getMemberRole().equals("seller")){
                SellerVo sellerVo = memberServiceClient.findBySellerId(feedComment.getMemberId());
                commentDetail.setMemberName(sellerVo.getSellerName());
                commentDetail.setMemberProfileImg(sellerVo.getSellerProfileImg());
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
        feedComment.setFeedCommentStatus(true);

        FeedComment savedFeedComment = feedCommentRepository.save(feedComment);
        return savedFeedComment.getFeedCommnetId();
    }

    @Override
    public Long modifyComment(CommentInfoDto commentInfoDto) {

        Optional<FeedComment> feedComment = feedCommentRepository.findById(commentInfoDto.getFeedCommentId());

        if(feedComment.isPresent()){
            if(feedComment.get().getMemberId().equals(commentInfoDto.getMemberId())) {
                feedComment.get().setFeedCommentContent(commentInfoDto.getFeedCommentContent());
                feedComment.get().setFeedCommentModifiedAt(dateUtils.transDate(env.getProperty("dateutils.format")));

                return feedComment.get().getFeedCommnetId();
            }
        }

        return null;
    }

    @Override
    public Long deleteComment(CommentInfoDto commentInfoDto) {

        Optional<FeedComment> feedComment = feedCommentRepository.findById(commentInfoDto.getFeedCommentId());

        if(feedComment.isPresent()){
            if(feedComment.get().getMemberId().equals(commentInfoDto.getMemberId())) {
                feedComment.get().setFeedCommentStatus(false);

                return feedComment.get().getFeedCommnetId();
            }
        }
        return null;
    }
}
