package com.storysoksok.backend.service.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.storysoksok.backend.exception.CustomException;
import com.storysoksok.backend.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3Service {
    @Value("${cloud.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;

    /**
     * 이미지파일을 버킷에 업로드하고 저장한 이미지파일의 이름(랜덤한 UUID가 붙은)을 반환해주는 메서드입니다.
     */
    /** 파일 이름 생성(확장자 보존) */
    private String generateRandomFilename(String origin) {
        String ext = origin == null ? "" : origin.substring(origin.lastIndexOf("."));
        return UUID.randomUUID() + ext;
    }

    public String uploadBytes(byte[] bytes, String originalName, String contentType) {
        String key = generateRandomFilename(originalName);

        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(bytes.length);
        meta.setContentType(contentType);

        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes)) {
            amazonS3.putObject(bucket, key, bis, meta);
        } catch (AmazonServiceException | IOException e) {
            throw new CustomException(ErrorCode.S3_UPLOAD_ERROR);
        }

        log.debug("Uploaded to S3: {}", key);
        return amazonS3.getUrl(bucket, key).toString();   // URL 직접 리턴해도 편함
    }
}
