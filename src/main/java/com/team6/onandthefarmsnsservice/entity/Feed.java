package com.team6.onandthefarmsnsservice.entity;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Builder
@Slf4j
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Feed {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long feedId;

    private Long memberId;

    private Integer memberRoll; // 1: user 2: seller

    private Long productId;

    private String feedTitle;

    private String feedContent;

    private Integer feedViewCount;

    private Integer feedLikeCount;

    private String feedCreateAt;

    private String feedUpdateAt;

    private Integer feedShareCount;

    private Boolean feedStatus;

}
