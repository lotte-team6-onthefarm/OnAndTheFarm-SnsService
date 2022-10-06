package com.team6.onandthefarmsnsservice.feignclient;

import com.team6.onandthefarmsnsservice.vo.user.Following;
import com.team6.onandthefarmsnsservice.vo.user.Seller;
import com.team6.onandthefarmsnsservice.vo.user.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.List;

@FeignClient(name = "member-service")
public interface MemberServiceClient {
    @GetMapping("/api/user/member-service/user/{user-no}")
    public User findByUserId(@PathVariable("user-no")Long userId);

    @GetMapping("/api/seller/member-service/seller/{seller-no}")
    public Seller findBySellerId(@PathVariable("seller-no")Long sellerId);

    @GetMapping("/api/user/member-service/following/{member-no}")
    public List<Following> findByFollowingMemberId(@PathVariable("member-no")Long memberId);

}
