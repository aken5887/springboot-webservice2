package com.toyproject.book.springboot.web.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PostsListRequestDto {
    private String type;
    private String keyword;

    @Builder
    public PostsListRequestDto(String type, String keyword) {
        this.type = type;
        this.keyword = keyword;
    }

    @Override
    public String toString() {
        return "PostsListRequestDto{" +
                "type='" + type + '\'' +
                ", keyword='" + keyword + '\'' +
                '}';
    }
}
