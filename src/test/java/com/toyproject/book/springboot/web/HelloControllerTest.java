package com.toyproject.book.springboot.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/** @ExtendWith(SpringExtension.class)
 * 스프링 부트 테스트와 JUnit 사이에 연결자 역할을 함
 */
@ExtendWith(SpringExtension.class)
/** @ WebMvcTest
 * Spring MVC(Web)에 집중할 수 있는 어노테이션
 * 선언할 경우 @Controller, @ControllerAdvice 등 사용
 * 단, @Service, @Component, @Repository 등은 사용 불가
 */
@WebMvcTest(controllers = HelloController.class)
class HelloControllerTest {

    @Autowired
    /** MockMvc
     *  웹 API를 테스트할 때 사용
     *  이 클래스를 통해 HTTP GET, POST 등에 대한 API 테스트를 할 수 있음.
     */
    private MockMvc mvc;

    @Test
    public void hello가_리턴된다() throws Exception {
        String hello = "hello";
        mvc.perform(get("/hello"))      // 체이닝 지원
                .andExpect(status().isOk())
                .andExpect(content().string("hello"));
    }

    @Test
    public void helloDto가_리턴된다() throws Exception {
        String name = "hello";
        int amount = 100;

        mvc.perform(get("/hello/dto")
                .param("name", name)
                .param("amount", String.valueOf(amount))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.amount").value(amount));
    }
}