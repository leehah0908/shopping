package com.shopping.orderservice.product.service;

import com.shopping.orderservice.product.dto.request.ReqProductSaveDto;
import com.shopping.orderservice.product.dto.response.ResProductDto;
import com.shopping.orderservice.product.entity.Product;
import com.shopping.orderservice.product.repository.ProductRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;


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

    public Page<ResProductDto> productList(Pageable pageable) {
        System.out.println(pageable);
        Page<Product> products = productRepository.findAll(pageable);

        // 클라이언트에 페이징에 필요한 데이터를 제공하기 위해 Page 객체를 넘겨야 함
        // Page 안에 Entity가 들어있기 때문에 dto로 변환해서 넘겨야 함 (page 객체는 유지)
        // map을 통해 product를 dto로 변환해서 리턴
        return products.map(Product::toProductDto);
    }
}
