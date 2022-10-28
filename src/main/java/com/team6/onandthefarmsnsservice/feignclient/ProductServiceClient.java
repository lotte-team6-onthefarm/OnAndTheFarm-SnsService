package com.team6.onandthefarmsnsservice.feignclient;

import java.util.List;

import com.team6.onandthefarmsnsservice.vo.feed.AddableProductResponse;
import com.team6.onandthefarmsnsservice.vo.product.ProductVo;
import com.team6.onandthefarmsnsservice.vo.product.WishVo;
import com.team6.onandthefarmsnsservice.vo.profile.WishProductListResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@FeignClient(name = "product-service")
public interface ProductServiceClient {

	@GetMapping("/api/user/product/product-service/product/{user-no}")
	public List<WishProductListResponse> findWishProductListByMember(@PathVariable("user-no") Long memberId);

	@GetMapping("/api/user/product/product-service/list/add/{seller-no}")
	public List<AddableProductResponse> findAddableProductList(@PathVariable("seller-no") Long memberId);

	@GetMapping("/api/user/product/product-service/wish-list/{user-no}")
	public List<WishVo> findWishListByMemberId(PageRequest pageRequest, @PathVariable("user-no")Long memberId);

	@GetMapping("/api/user/product/product-service/product/{product-no}")
	public ProductVo findProductByProductId(@PathVariable("product-no") Long productId);
}
