package com.toyproject.book.springboot.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toyproject.book.springboot.domian.posts.Posts;
import com.toyproject.book.springboot.domian.posts.PostsRepository;
import com.toyproject.book.springboot.web.dto.PostsSaveRequestDto;
import com.toyproject.book.springboot.web.dto.PostsUpdateRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    // MockMvc 추가
    @BeforeEach
    public void setup(){
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @AfterEach
    public void tearDown() throws Exception {
        postsRepository.deleteAll();
    }

//    @Test
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
    @WithMockUser(roles="USER")
    public void Posts_등록된다_MockMvc() throws Exception{
        // given
        String title = "title_tile_title";
        String content = "content";

        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author("author")
                .build();

        String url = "http://localhost:"+port+"/api/v1/posts";

        // when
        MvcResult s = mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andReturn();

        String savedId = s.getResponse().getContentAsString();
        // then
        Posts all = postsRepository.findById(Long.parseLong(savedId)).orElse(new Posts());
        System.out.println(all.toString());
        assertThat(all.getTitle()).isEqualTo(title);
        assertThat(all.getContent()).isEqualTo(content);
    }

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

    @WithMockUser(roles="USER")
    @Test
    public void Posts_수정된다_MockMvc() throws Exception {
        // given
        Posts savePosts = postsRepository.save(Posts.builder()
                        .title("title")
                        .author("author")
                        .content("content")
                        .build());

        Long savedId = savePosts.getId();
        String newTitle = "new_title_title";
        String newContent = "new_content_content";

        PostsUpdateRequestDto dto = PostsUpdateRequestDto.builder()
                                    .title(newTitle)
                                    .content(newContent)
                                    .build();

        String url = "http://localhost:"+port+"/api/v1/posts/"+savedId;

        //when
        mvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isOk());

        //then
        Posts expected = postsRepository.findById(savedId).orElseGet(()->new Posts());
        assertThat(expected.getTitle()).isEqualTo(newTitle);
        assertThat(expected.getContent()).isEqualTo(newContent);
    }

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

    @WithMockUser(roles="USER")
    @Test
    public void Posts_삭제된다_MockMvc() throws Exception {
        //given
        Posts savedPost = postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());
        Long savedId = savedPost.getId();

        // when
        String url = "http://localhost:"+port+"/api/v1/posts/"+savedId;
        mvc.perform(delete(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // then
        assertThat(!postsRepository.findById(savedId).isPresent());
    }

}