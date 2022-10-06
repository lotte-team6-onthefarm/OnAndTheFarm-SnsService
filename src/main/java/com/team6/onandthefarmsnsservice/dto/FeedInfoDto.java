package com.team6.onandthefarmsnsservice.dto;

import com.team6.onandthefarmsnsservice.vo.ImageProduct;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Data
public class FeedInfoDto {

    private String feedTitle;

    private String feedContent;

    private List<MultipartFile> feedImgSrcList;

    private List<ImageProduct> feedProductIdList;

}
