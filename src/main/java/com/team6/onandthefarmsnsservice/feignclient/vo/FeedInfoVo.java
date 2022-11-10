package com.team6.onandthefarmsnsservice.feignclient.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedInfoVo {

	private Long feedId;

	private Long memberId;

	private String memberRole;

	private String feedTitle;

	private String feedContent;

	private Integer feedViewCount;

	private Integer feedLikeCount;

	private String feedCreateAt;

	private String feedUpdateAt;

	private Integer feedShareCount;

	private Boolean feedStatus; // true : 피드게시중 / false : 피드 삭제

	private Integer feedScrapCount;

	private Integer feedCommentCount;

	private Long feedNumber;

}
