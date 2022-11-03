package com.team6.onandthefarmsnsservice.feignclient.controller;

import com.team6.onandthefarmsnsservice.feignclient.service.OrderServiceClientService;
import com.team6.onandthefarmsnsservice.feignclient.vo.FeedVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class OrderServiceClientController {

    private final OrderServiceClientService orderServiceClientService;

    @Autowired
    public OrderServiceClientController(OrderServiceClientService orderServiceClientService){
        this.orderServiceClientService = orderServiceClientService;
    }

    @GetMapping("/api/user/feed/sns-service/{feed-no}")
    FeedVo findByFeedNumber(@PathVariable("feed-no") Long feedNumber){
        return orderServiceClientService.findFeedByFeedNumber(feedNumber);
    }
}
