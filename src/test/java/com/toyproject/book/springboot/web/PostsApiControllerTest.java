package com.toyproject.book.springboot.web;

import com.toyproject.book.springboot.domian.posts.Posts;
import com.toyproject.book.springboot.domian.posts.PostsRepository;
import com.toyproject.book.springboot.web.dto.PostsSaveRequestDto;
import com.toyproject.book.springboot.web.dto.PostsUpdateRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @AfterEach
    public void tearDown() throws Exception {
        postsRepository.deleteAll();
    }

    @Test
    public void Posts_등록된다() throws Exception{
        // given
        String title = "title";
        String content = "content";

        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author("author")
                .build();

        String url = "http://localhost:"+port+"/api/v1/posts";

        // when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }

    @Test
    public void Posts_수정된다() throws Exception{
        //given
        Posts savedPosts = postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());

        Long savedId = savedPosts.getId();
        String expectedTitle = "new_title";
        String expectedContent = "new_content";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();

        String url = "http://localhost:"+port+"/api/v1/posts/"+savedId;

        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        // when
        // Long 타입으로 할 경우 Long -> JSON 파싱 에러 발생
        ResponseEntity<Object> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Object.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
//        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        Posts result = postsRepository.findById(savedId).get();//orElse(new Posts());
        assertThat(result.getTitle()).isEqualTo(expectedTitle);
        assertThat(result.getContent()).isEqualTo(expectedContent);

    }

    @Test
    public void Posts_삭제된다() {
        // given
       Posts savedPost = postsRepository.save(Posts.builder()
                                                    .title("테스트")
                                                    .content("테스트2")
                                                    .build());
       Long savedId = savedPost.getId();

       // when
        String url = "http://localhost:"+port+"/api/v1/posts/"+savedId;
        ResponseEntity<Object> responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, null, Object.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(savedId.intValue());

        Optional<Posts> result = postsRepository.findById(savedId);
        assertThat(!result.isPresent());
    }

}