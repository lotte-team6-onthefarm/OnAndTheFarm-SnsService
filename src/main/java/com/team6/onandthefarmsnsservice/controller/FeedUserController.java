package com.team6.onandthefarmsnsservice.controller;

import com.team6.onandthefarmsnsservice.utils.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user/feed")
public class FeedUserController {

    @GetMapping("list")
    public ResponseEntity<BaseResponse<List<FeedResponse>>> findByFeedList(){

    }
}
