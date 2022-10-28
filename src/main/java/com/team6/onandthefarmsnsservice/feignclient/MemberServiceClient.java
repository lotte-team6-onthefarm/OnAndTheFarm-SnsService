package com.team6.onandthefarmsnsservice.feignclient;

import com.team6.onandthefarmsnsservice.vo.user.Following;
import com.team6.onandthefarmsnsservice.vo.user.SellerVo;
import com.team6.onandthefarmsnsservice.vo.user.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "member-service")
public interface MemberServiceClient {
    @GetMapping("/api/user/member-service/user/{user-no}")
    User findByUserId(@PathVariable("user-no")Long userId);

    @GetMapping("/api/seller/members/member-service/{seller-no}")
    SellerVo findBySellerId(@PathVariable("seller-no")Long sellerId);

    @GetMapping("/api/user/member-service/following/{member-no}")
    List<Following> findByFollowingMemberId(@PathVariable("member-no")Long memberId);
}
