package com.team6.onandthefarmsnsservice.vo.product;

import lombok.*;
import org.springframework.data.domain.PageRequest;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishPageVo {
    private Integer pageNumber;
    private PageRequest pageRequest;
}
