package com.toyproject.book.springboot.web.dto;

import com.toyproject.book.springboot.domian.posts.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Getter
@NoArgsConstructor
public class PostsSaveRequestDto {
    private String title;
    private String content;
    private String author;
    private String fileId;

    @Builder
    public PostsSaveRequestDto(String title, String content, String author, String fileId) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.fileId = fileId;
    }

    public Posts toEntity(){
        return Posts.builder()
                .title(title)
                .content(content)
                .author(author)
                .fileId(fileId)
                .build();
    }
}
