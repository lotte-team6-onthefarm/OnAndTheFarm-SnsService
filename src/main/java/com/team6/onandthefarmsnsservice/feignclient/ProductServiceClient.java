package com.team6.onandthefarmsnsservice.feignclient;

import java.util.List;

import com.team6.onandthefarmsnsservice.vo.product.*;

import io.swagger.models.auth.In;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "product-service")
public interface ProductServiceClient {

	@GetMapping("/api/user/product/product-service/wish/{user-no}")
	List<WishListResponse> findWishProductListByMember(@PathVariable("user-no") Long memberId);

	@GetMapping("/api/user/product/product-service/wish-list/{user-no}")
	List<WishVo> findWishListByMemberId(@RequestBody WishPageVo wishPageVo, @PathVariable("user-no")Long memberId);

	@GetMapping("/api/user/product/product-service/product/{product-no}")
	ProductVo findProductByProductId(@PathVariable("product-no") Long productId);

	@GetMapping("/api/seller/product/product-service/product/{seller-no}")
	List<ProductVo> findBySellerId(@PathVariable("seller-no") Long sellerId);

	@GetMapping("/api/user/product/product-service/review/{product-no}")
	List<ReviewVo> findReviewByProductId(@PathVariable("product-no") Long productId);
}
