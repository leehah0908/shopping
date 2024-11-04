package com.shopping.orderservice.ordering.service;

import com.shopping.orderservice.common.auth.TokenUserInfo;
import com.shopping.orderservice.ordering.dto.request.ReqOrderSaveDto;
import com.shopping.orderservice.ordering.dto.response.ResOrderListDto;
import com.shopping.orderservice.ordering.entity.OrderDetail;
import com.shopping.orderservice.ordering.entity.Orders;
import com.shopping.orderservice.ordering.repository.OrderRepository;
import com.shopping.orderservice.product.entity.Product;
import com.shopping.orderservice.product.repository.ProductRepository;
import com.shopping.orderservice.user.entity.User;
import com.shopping.orderservice.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public Orders orderSave(TokenUserInfo userInfo, List<ReqOrderSaveDto> dtoList) {

        User user = userRepository.findByEmail(userInfo.getEmail()).orElseThrow(
                () -> new EntityNotFoundException("회원 정보 찾을 수 없음")
        );

        // 주문 객체 생성
        Orders orders = Orders.builder()
                .user(user)
                .orderDetails(new ArrayList<>())
                .build();

        // 주문 상세 내역 처리
        for (ReqOrderSaveDto req : dtoList) {
            Product product = productRepository.findById(req.getProductId()).orElseThrow(() ->
                    new EntityNotFoundException("상품 정보를 찾을 수 없음")
            );

            int quantity = req.getQuantity();

            if (product.getStockQuantity() < quantity) {
                throw new IllegalArgumentException("재고 부족");

            }

            product.setStockQuantity(product.getStockQuantity() - quantity);

            // 주문 상세 내역 엔터티 생성
            OrderDetail orderDetail = OrderDetail.builder()
                    .product(product)
                    .orders(orders)
                    .quantity(quantity)
                    .build();

            // 주문 내열 리스트에 상세 내역을 add하기
            // cascadeType.PERSIST로 세팅했기 때문에 함께 add가 진행됨.
            orders.getOrderDetails().add(orderDetail);
        }

        // order를 세이브하면 내부에 있는 detail 리스트도 함께 insert됨
        return orderRepository.save(orders);
    }

    public List<ResOrderListDto> myOrder(TokenUserInfo userInfo) {

        User user = userRepository.findByEmail(userInfo.getEmail()).orElseThrow(
                () -> new EntityNotFoundException("회원 정보를 찾을 수 없습니다."));

        List<Orders> orders = orderRepository.findByUser(user).orElseThrow(
                () -> new EntityNotFoundException("주문 점보가 없습니다.")
        );


        List<ResOrderListDto> result = orders.stream()
                .map(Orders::toResOrderListDto)
                .toList();

        return result;

        /*
         OrderingListResDto -> orderDetailDto (static 내부 클래스)
         {
            id: 주문번호,
            userEmail: 주문한 사람 이메일,
            orderStatus: 주문 상태
            orderDetails: [
                {
                    id: 주문상세번호,
                    productName: 상품명,
                    count: 수량
                },
                {
                    id: 주문상세번호,
                    productName: 상품명,
                    count: 수량
                },
                {
                    id: 주문상세번호,
                    productName: 상품명,
                    count: 수량
                }
                ...
            ]
         }
         */
    }

    public List<ResOrderListDto> allList() {
        List<Orders> orders = orderRepository.findAll();

        return orders.stream()
                .map(Orders::toResOrderListDto)
                .toList();
    }
}
