package com.team6.onandthefarmsnsservice.feignclient;

import com.team6.onandthefarmsnsservice.vo.feed.AddableProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "order-service")
public interface OrderServiceClient {

    @GetMapping("/api/user/order/order-service/list/add/{user-no}")
    public List<AddableProductResponse> findAddableProductList(@PathVariable("user-no") Long memberId);
}
