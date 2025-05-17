package com.storysoksok.backend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // GLOBAL

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 문제가 발생했습니다."),

    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근이 거부되었습니다."),

    // AUTH

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증에 실패했습니다."),

    MISSING_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "인증 토큰이 필요합니다."),

    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 엑세스 토큰입니다."),

    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다."),

    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "엑세스 토큰이 만료되었습니다."),

    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 만료되었습니다."),

    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "리프레시 토큰을 찾을 수 없습니다."),

    TOKEN_BLACKLISTED(HttpStatus.UNAUTHORIZED, "블랙리스트처리된 토큰이 요청되었습나다."),

    COOKIES_NOT_FOUND(HttpStatus.NOT_FOUND, "쿠키가 존재하지 않습니다."),

    // MEMBER
    DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST, "이미 가입된 이메일입니다"),

    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "회원을 찾을 수 없습니다."),

    INVALID_MEMBER_TYPE(HttpStatus.BAD_REQUEST, "잘못된 회원 자격입니다"),

    INVALID_MEMBER_ROLE_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 회원 권한 요청입니다."),


    // FAIRY_TALE
    INTERNAL_IMAGE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "이미지를 불러오는 도중 알수없는 문제가 발생했습니다."),


    // S3
    S3_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR ,"이미지 업로드중 에러가 발생했습니다.")
    ;

    private final HttpStatus status;
    private final String message;
}
