package com.team6.onandthefarmsnsservice.feignclient;

import com.team6.onandthefarmsnsservice.vo.orders.AddableOrderProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "order-service")
public interface OrderServiceClient {

    @GetMapping("/api/user/order/order-service/list/add/{user-no}")
    List<AddableOrderProductResponse> findAddableOrderProductList(@PathVariable("user-no") Long memberId);
}
