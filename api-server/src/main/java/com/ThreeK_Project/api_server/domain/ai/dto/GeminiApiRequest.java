package com.ThreeK_Project.api_server.domain.ai.dto;

import java.util.List;

public record GeminiApiRequest(List<Content> contents) {
    public record Content(List<Part> parts) {}
    public record Part(String text) {}
}