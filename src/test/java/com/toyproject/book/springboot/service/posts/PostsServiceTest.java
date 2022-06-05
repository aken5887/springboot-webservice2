package com.toyproject.book.springboot.service.posts;

import com.toyproject.book.springboot.domian.posts.Posts;
import com.toyproject.book.springboot.web.dto.PostsListResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class PostsServiceTest {

    @Autowired
    private PostsService postsService;

    @Test
    public void POSTS_전체조회(){
        List<PostsListResponseDto> postsList = postsService.findAllPosts();
        assertThat(postsList.size()).isGreaterThanOrEqualTo(0);
    }
}