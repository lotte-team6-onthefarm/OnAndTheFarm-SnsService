package com.team6.onandthefarmsnsservice.vo.user;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerVo {
    private Long sellerId;

    private String sellerEmail;

    //private String sellerPassword;

    private String sellerZipcode;

    private String sellerAddress;

    private String sellerAddressDetail;

    private String sellerPhone;

    private String sellerName;

    private String sellerShopName;

    private String sellerBusinessNumber;

    private String sellerRegisterDate;

    private Boolean sellerIsActived;

    private String role;
}
