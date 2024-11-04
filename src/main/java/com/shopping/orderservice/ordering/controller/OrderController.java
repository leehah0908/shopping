package com.shopping.orderservice.ordering.controller;

import com.shopping.orderservice.common.auth.TokenUserInfo;
import com.shopping.orderservice.common.dto.CommonResDto;
import com.shopping.orderservice.ordering.dto.request.ReqOrderSaveDto;
import com.shopping.orderservice.ordering.dto.response.ResOrderListDto;
import com.shopping.orderservice.ordering.entity.Orders;
import com.shopping.orderservice.ordering.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    // 개인 주문 내역 조회
    @GetMapping("/myorder")
    public ResponseEntity<?> getMyOrder(@AuthenticationPrincipal TokenUserInfo userInfo){
        List<ResOrderListDto> dtos = orderService.myOrder(userInfo);

        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "정상 조회 완료", dtos);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 전체 주문 내역 조회
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllList(){
        List<ResOrderListDto> dtos = orderService.allList();

        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "모든 주문 정보 조회 완료", dtos);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }
}
