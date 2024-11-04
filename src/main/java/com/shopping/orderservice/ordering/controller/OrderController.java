package com.shopping.orderservice.ordering.controller;

import com.shopping.orderservice.common.auth.TokenUserInfo;
import com.shopping.orderservice.common.dto.CommonResDto;
import com.shopping.orderservice.ordering.dto.request.ReqOrderSaveDto;
import com.shopping.orderservice.ordering.entity.Orders;
import com.shopping.orderservice.ordering.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    // @AuthenticationPrincipal: 메서드 호출시에 사용자 인증 정보를 전달 <- ContextHolder에서 정보를 가져옴
    public ResponseEntity<?> create(@AuthenticationPrincipal TokenUserInfo userInfo,
                                    @RequestBody List<ReqOrderSaveDto> dtoList){

        Orders orders = orderService.orderSave(userInfo, dtoList);

        CommonResDto resDto = new CommonResDto(HttpStatus.CREATED, "정상 주문 완료", orders.getOrderId());

        return new ResponseEntity<>(resDto, HttpStatus.CREATED);
    }
}
