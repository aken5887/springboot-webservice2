package com.toyproject.book.springboot.domian.posts;

import com.toyproject.book.springboot.domian.BaseTimeEntity;
import com.toyproject.book.springboot.web.dto.PostsSaveRequestDto;
import com.toyproject.book.springboot.web.dto.PostsUpdateRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Posts extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String author;

    private String fileId;

    @Builder
    public Posts(String title, String content, String author, String fileId) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.fileId = fileId;
    }

    public void update(PostsUpdateRequestDto requestDto){
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.fileId = requestDto.getFileId();
    }

    @Override
    public String toString() {
        return "Posts{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
