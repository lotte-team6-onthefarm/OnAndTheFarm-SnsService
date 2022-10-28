package com.team6.onandthefarmsnsservice.feignclient;

import java.util.List;

import com.team6.onandthefarmsnsservice.vo.product.ProductVo;
import com.team6.onandthefarmsnsservice.vo.product.ReviewVo;
import com.team6.onandthefarmsnsservice.vo.product.WishListResponse;
import com.team6.onandthefarmsnsservice.vo.product.WishVo;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@FeignClient(name = "product-service")
public interface ProductServiceClient {

	//구현 필요 -yewon-
	@GetMapping("/api/user/product/product-service/wish/{user-no}")
	public List<WishListResponse> findWishProductListByMember(@PathVariable("user-no") Long memberId);

	//구현 필요 -jiny-
	@GetMapping("/api/user/product/product-service/wish-list/{user-no}")
	public Page<WishVo> findWishListByMemberId(PageRequest pageRequest, @PathVariable("user-no")Long memberId);

	//구현 필요 -jiny-
	@GetMapping("/api/user/product/product-service/product/{product-no}")
	public ProductVo findProductByProductId(@PathVariable("product-no") Long productId);

	//구현 필요 -yewon-
	@GetMapping("/api/seller/product/product-service/product/{seller-no}")
	List<ProductVo> findBySellerId(@PathVariable("seller-no") Long sellerId);

	//구현 필요 -yewon-
	@GetMapping("/api/user/product/product-service/review/{product-no}")
	List<ReviewVo> findReviewByProductId(@PathVariable("product-no") Long productId);


}
