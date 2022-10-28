package com.team6.onandthefarmsnsservice.feignclient;

import com.team6.onandthefarmsnsservice.vo.user.FollowingVo;
import com.team6.onandthefarmsnsservice.vo.user.SellerVo;

import com.team6.onandthefarmsnsservice.vo.user.UserVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "member-service")
public interface MemberServiceClient {
    @GetMapping("/api/user/members/member-service/{user-no}")
    UserVo findByUserId(@PathVariable("user-no")Long userId);

    @GetMapping("/api/seller/members/member-service/{seller-no}")
    SellerVo findBySellerId(@PathVariable("seller-no")Long sellerId);

    @GetMapping("/api/user/members/member-service/{member-no}")
    List<FollowingVo> findByFollowingMemberId(@PathVariable("member-no")Long memberId);

    //구현아직임 -jiny-
    @GetMapping("/api/user/members/member-service/get-user/{member-no}")
    UserVo getUserByUserId(@PathVariable("member-no")Long memberId);

    //구현아직임 -jiny-
    @GetMapping("/api/seller/members/member-service/get-seller/{member-no}")
    SellerVo getSellerBySellerId(@PathVariable("member-no")Long memberId);

    //구현 필요 -yewon-
    @GetMapping("/api/user/members/member-service/following")
    FollowingVo findByFollowingMemberIdAndFollowerMemberId(@RequestParam Long followingMemberId, @RequestParam Long followerMemberId);

}
