package com.storysoksok.backend.dto.gpt.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GptCallRequest {
    private String model;
    private String message;

    @Builder
    public GptCallRequest(String model, String message) {
        this.model = model;
        this.message = message;
    }
}
