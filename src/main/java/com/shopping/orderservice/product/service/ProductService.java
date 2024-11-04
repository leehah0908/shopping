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
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public List<ResProductDto> productList(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        List<Product> content = products.getContent();

        return content.stream()
                .map(Product::toProductDto)
                .collect(Collectors.toList());
    }
}
