package com.toyproject.book.springboot.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    FILE_SIZE_EXCEED(HttpStatus.BAD_REQUEST, "400", "FILE_SIZE_EXCEED");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
