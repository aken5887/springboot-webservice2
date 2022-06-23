package com.toyproject.book.springboot.config;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
@NoArgsConstructor
public class ErrorResponse {

    private HttpStatus status;
    private String code;
    private String message;

    @Builder
    public ErrorResponse(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public static ErrorResponse of(ErrorCode errorCode){
       return ErrorResponse.builder()
               .status(errorCode.getHttpStatus())
               .code(errorCode.getCode())
               .message(errorCode.getMessage())
               .build();
   }
}
