package com.toyproject.book.springboot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler({MaxUploadSizeExceededException.class, SizeLimitExceededException.class})
//    protected ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(Exception e){
//        log.info("handleMaxUploadSizeExceededException", e);
//        ErrorResponse response = ErrorResponse.of(ErrorCode.FILE_SIZE_EXCEED);
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    protected ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(
            MaxUploadSizeExceededException e) {
        log.info("handleMaxUploadSizeExceededException", e);

        ErrorResponse response = ErrorResponse.of(ErrorCode.FILE_SIZE_EXCEED);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
