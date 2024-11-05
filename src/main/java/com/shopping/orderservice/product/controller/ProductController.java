package com.shopping.orderservice.product.controller;

import com.shopping.orderservice.common.dto.CommonResDto;
import com.shopping.orderservice.product.dto.request.ReqProductSaveDto;
import com.shopping.orderservice.product.dto.response.ResProductDto;
import com.shopping.orderservice.product.entity.Product;
import com.shopping.orderservice.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    // 요청에 이미지가 함께 들어옴 -> 이미지를 처리하는 방식이 두가지가 있음
    // 1. js의 FormData 객체를 통해서 모든 데이터를 전달 (multipart/form-data 형식으로 전달)
    // 2. JSON 형태로 전달 (이미지를 Base64 인코딩을 통해 문자열로 변환해서 전달)
    public ResponseEntity<?> create(@Valid ReqProductSaveDto dto) {

        Product product = productService.create(dto);

        CommonResDto resDto = new CommonResDto(HttpStatus.CREATED, "상품 등록 완료", product.getProductId());

        return new ResponseEntity<>(resDto, HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<?> list(Pageable pageable) {

        List<ResProductDto> products = productService.productList(pageable);

        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "상품 정보 조회 완료", products);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }
}
