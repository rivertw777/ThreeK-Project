package com.ThreeK_Project.api_server.domain.ai.dto;

import java.util.List;

public record GeminiApiResponse(
        List<Candidate> candidates,
        UsageMetadata usageMetadata
) {
    public record Candidate(
            Content content,
            String finishReason,
            int index,
            List<SafetyRating> safetyRatings
    ) {}

    public record Content(
            List<Part> parts,
            String role
    ) {}

    public record Part(
            String text
    ) {}

    public record SafetyRating(
            String category,
            String probability
    ) {}

    public record UsageMetadata(
            int promptTokenCount,
            int candidatesTokenCount,
            int totalTokenCount
    ) {}
}
