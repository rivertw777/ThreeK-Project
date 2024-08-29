package com.ThreeK_Project.api_server.domain.ai.service;

import com.ThreeK_Project.api_server.domain.ai.dto.CreateProductDescriptionRequest;
import com.ThreeK_Project.api_server.domain.ai.dto.GeminiApiRequest;
import com.ThreeK_Project.api_server.domain.ai.dto.GeminiApiResponse;
import com.ThreeK_Project.api_server.domain.ai.dto.ProductDescriptionResponse;
import com.ThreeK_Project.api_server.domain.ai.entity.AiRequestLog;
import com.ThreeK_Project.api_server.domain.ai.repository.AiRequestLogRepository;
import com.ThreeK_Project.api_server.global.exception.ApplicationException;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AiRequestLogService {

    @Value("${google.api.key}")
    private String apiKey;

    private final AiRequestLogRepository aiRequestLogRepository;
    private final RestTemplate restTemplate;

    // 상품 설명 AI 요청 생성
    @Transactional
    public ProductDescriptionResponse getProductDescription(CreateProductDescriptionRequest requestParam) {
        String question = requestParam.question() + " 답변은 50자 이내로 해줘.";
        GeminiApiRequest request = makeGeminiApiRequest(question);
        GeminiApiResponse response = postApiRequest(request);
        String answer = response.candidates().get(0).content().parts().get(0).text();

        saveAiRequestLog(question, answer);
        return new ProductDescriptionResponse(answer);
    }

    private GeminiApiRequest makeGeminiApiRequest(String question) {
        GeminiApiRequest.Part part = new GeminiApiRequest.Part(question);
        GeminiApiRequest.Content content = new GeminiApiRequest.Content(Arrays.asList(part));
        return new GeminiApiRequest(Arrays.asList(content));
    }

    private GeminiApiResponse postApiRequest(GeminiApiRequest request) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=" + apiKey;
        try {
            return restTemplate.postForObject(url, request, GeminiApiResponse.class);
        } catch (Exception e) {
            throw new ApplicationException("API 요청 실패: " + e.getMessage(), e);
        }
    }

    private void saveAiRequestLog(String question, String answer) {
        AiRequestLog aiRequestLog = AiRequestLog.createAIRequestLog(question, answer);
        aiRequestLogRepository.save(aiRequestLog);
    }

}
