package com.storysoksok.backend.service.gpt;

import com.openai.client.OpenAIClient;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.models.images.Image;
import com.openai.models.images.ImageGenerateParams;
import com.openai.models.images.ImagesResponse;
import com.storysoksok.backend.exception.CustomException;
import com.storysoksok.backend.exception.ErrorCode;
import com.storysoksok.backend.service.s3.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GptService {
    @Value("${openai.model}")
    private String model;
    private final OpenAIClient openAIClient;
    private final S3Service s3Service;

    /**
     * 동화 중반부까지의 내용을 반환해주는 메서드
     */
    public Optional<String> generateFirstFairyTale(String prompt) {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model(model)
                .addSystemMessage(prompt)
                .maxCompletionTokens(2048)
                .build();

        ChatCompletion res = openAIClient.chat()
                .completions()
                .create(params);

        return res.choices().get(0).message().content();
    }

    /**
     * 이미지 생성을 위한 메서드 (총 4장)
     */
    public String generatePicture(String prompt) {
        String imageUrl = "";

        ImageGenerateParams imageGenerateParams = ImageGenerateParams.builder()
                .prompt(prompt)
                .model("gpt-image-1")
                .size(ImageGenerateParams.Size._1024X1024)
                .n(1)
                .build();


        ImagesResponse response = openAIClient.images().generate(imageGenerateParams);

        List<Image> images = response.data()
                .orElseThrow(() -> new CustomException(ErrorCode.INTERNAL_IMAGE_ERROR));

        for (Image img : images) {
            // b64Json() Optional 해제
            String b64 = img.b64Json()
                    .orElseThrow(() -> new CustomException(ErrorCode.INTERNAL_IMAGE_ERROR));

            byte[] bytes = Base64.getDecoder().decode(b64);

            // gpt-image-1은 PNG 형식으로 내려옴
            imageUrl = s3Service.uploadBytes(bytes, "image.png", "image/png");
        }

        return imageUrl;   // 프론트는 <img src="...">로 바로 사용
    }
}
