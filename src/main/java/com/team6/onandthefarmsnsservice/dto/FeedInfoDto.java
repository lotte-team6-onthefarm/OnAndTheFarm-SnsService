package com.team6.onandthefarmsnsservice.dto;

import com.team6.onandthefarmsnsservice.vo.imageProduct.ImageProductInfo;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class FeedInfoDto {

    private String feedTitle;

    private String feedContent;

    private List<MultipartFile> feedImgSrcList;

    private List<ImageProductInfo> feedProductIdList;

}
