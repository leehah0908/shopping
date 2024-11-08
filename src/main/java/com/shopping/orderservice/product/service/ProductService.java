package com.shopping.orderservice.product.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shopping.orderservice.product.dto.request.ReqProductSaveDto;
import com.shopping.orderservice.product.dto.request.ReqProductSearchDto;
import com.shopping.orderservice.product.dto.response.ResProductDto;
import com.shopping.orderservice.product.entity.Product;
import com.shopping.orderservice.product.repository.ProductRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.shopping.orderservice.product.entity.QProduct.product;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final JPAQueryFactory queryFactory;

    public Product create(@Valid ReqProductSaveDto dto) {

        MultipartFile productImage = dto.getProductImage();

        // 유니크 이름 만들기
        String imagePath = UUID.randomUUID() + "_" + productImage.getOriginalFilename();

        // 이미지 저장
        File file = new File("/Users/leehah/Playdata_backend/orderservice-backend/src/main/resources/product_imgs/" + imagePath);
        try {
            productImage.transferTo(file);
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패");
        }

        // 이미지 경로 저장
        Product product = dto.toProduct();
        product.setImagePath(imagePath);

        return productRepository.save(product);
    }

    public Page<ResProductDto> productList(ReqProductSearchDto dto, Pageable pageable) {

        // 검색기능 추가 전
//        Page<Product> products = productRepository.findAll(pageable);

        BooleanBuilder builder = new BooleanBuilder();

        if (dto.getSearchKeyword() != null) {
            // 상품 이름 검색 조건
            if (dto.getSearchType().equals("name")) {
                builder.and(product.productName.like("%" + dto.getSearchKeyword() + "%"));
            // 상품 카테고리 검색 조건
            } else if (dto.getSearchType().equals("category")) {
                builder.and(product.category.like("%" + dto.getSearchKeyword() + "%"));
            }
        }

        // queryDsl을 이용한 검색 및 페이징 처리
        List<Product> rawProducts = queryFactory
                .selectFrom(product)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 총 검색 결과 수를 구하는 쿼리 -> page 객체로 만들기 위해 필요
        long total = queryFactory
                .selectFrom(product)
                .where(builder)
                .fetchCount();

        // queryDsl로 조회한 내용을 모두 포함하는 page 객체 생성.
        Page<Product> products = new PageImpl<>(rawProducts, pageable, total);

        // 클라이언트에 페이징에 필요한 데이터를 제공하기 위해 Page 객체를 넘겨야 함
        // Page 안에 Entity가 들어있기 때문에 dto로 변환해서 넘겨야 함 (page 객체는 유지)
        // map을 통해 product를 dto로 변환해서 리턴
        return products.map(Product::toProductDto);
    }
}
