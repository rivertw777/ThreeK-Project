package com.ThreeK_Project.api_server.domain.ai.controller;

import com.ThreeK_Project.api_server.domain.ai.dto.CreateProductDescriptionRequest;
import com.ThreeK_Project.api_server.domain.ai.dto.ProductDescriptionResponse;
import com.ThreeK_Project.api_server.domain.ai.service.AiRequestLogService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class AiRequestLogController {

    private final AiRequestLogService aiRequestLogService;

    @Operation(summary = "상품 설명 AI 요청 생성")
    @PostMapping
    public ResponseEntity<ProductDescriptionResponse> getProductDescription(@Valid @RequestBody
                                                                            CreateProductDescriptionRequest requestParam) {
        ProductDescriptionResponse response = aiRequestLogService.getProductDescription(requestParam);
        return ResponseEntity.ok(response);
    }


}
