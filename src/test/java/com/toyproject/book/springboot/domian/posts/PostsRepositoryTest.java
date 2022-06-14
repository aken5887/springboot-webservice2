package com.toyproject.book.springboot.domian.posts;

import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @BeforeEach
    public void beforeCleanup() {
        System.out.println(" After method has called ");
        postsRepository.deleteAll();
    }
    @AfterEach
    public void cleanup() {
        System.out.println(" After method has called ");
        postsRepository.deleteAll();
    }

//    @Test
    public void 게시글저장_불러오기(){
        // given
        String title = "테스트 게시글";
        String content = "테스트 본문";

        postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .author("crimenut@naver.com")
                .build());

        // when
        List<Posts> postsList = postsRepository.findAll();

        // then
        Posts posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);
    }

//    @Test
    public void BaseTimeEntity_등록(){
        // given
        LocalDateTime now = LocalDateTime.now();

        Long savedId = postsRepository.save(Posts.builder()
                        .title("title")
                        .content("content")
                        .author("author")
                        .build()).getId();

        // when
        Posts savedPost = postsRepository.findById(savedId).get();

        // then
        System.out.println(">>>> createDate="+savedPost.getCreatedDate()+", modified Date ="+savedPost.getModifiedDate());
        System.out.println(">>> now "+now);
        assertThat(savedPost.getCreatedDate()).isAfter(now);
        assertThat(savedPost.getModifiedDate()).isAfter(now);
    }
}