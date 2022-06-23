package com.toyproject.book.springboot.web.dto;

import com.toyproject.book.springboot.domian.posts.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsUpdateRequestDto {

    private String title;
    private String content;

    private String fileId;

    @Builder
    public PostsUpdateRequestDto(String title, String content, String fileId) {
        this.title = title;
        this.content = content;
        this.fileId = fileId;
    }
}
