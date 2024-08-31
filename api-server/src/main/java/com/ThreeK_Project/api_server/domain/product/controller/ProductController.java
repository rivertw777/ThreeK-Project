package com.ThreeK_Project.api_server.domain.product.controller;


import com.ThreeK_Project.api_server.domain.product.dto.ProductResponse;
import com.ThreeK_Project.api_server.domain.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "가게 상품 단일 조회(role: x)")
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable("productId") UUID productId) {
        return ResponseEntity.ok().body(productService.getProduct(productId));
    }

    @Operation(summary = "상품 검색")
    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponse>> searchProduct(@RequestParam(value = "keyword", required = false) String keyword,
                                                               Pageable pageable) {
        Page<ProductResponse> result = productService.searchProduct(keyword, pageable);

        return ResponseEntity.ok().body(result);
    }

}
