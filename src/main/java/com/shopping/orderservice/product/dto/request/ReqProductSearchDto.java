package com.shopping.orderservice.product.dto.request;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReqProductSearchDto {

    private String searchType;
    private String searchKeyword;

}
