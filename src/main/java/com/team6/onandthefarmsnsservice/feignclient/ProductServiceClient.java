package com.team6.onandthefarmsnsservice.feignclient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.team6.onandthefarmsnsservice.vo.profile.product.WishProductListResponse;

@FeignClient(name = "product-service")
public interface ProductServiceClient {

	@GetMapping("/api/user/product-service/product/{user-no}")
	public List<WishProductListResponse> findWishProductListByMember(@PathVariable("user-no") Long memberId);
}
