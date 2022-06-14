package com.toyproject.book.springboot.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.h2.util.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IndexControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void 메인페이지_로딩(){
        // when
        String body = this.restTemplate.getForObject("/", String.class);

        //then
        assertThat(body).contains("스프링 부트 웹서비스2");
    }


//    @Test
    public void 카카오_코드_받기() {
        // code값
        final String clientId = "8686bc57b1236910b70b924fcfd89996";
        final String codeUrl = "https://kauth.kakao.com/oauth/authorize?client_id="+clientId+"&redirect_uri=http://localhost:8080/login/oauth2/code/kakao&response_type=code";

        ResponseEntity<String> response = this.restTemplate.exchange(codeUrl, HttpMethod.GET, null, String.class);
        System.out.println(response);
    }
//    @Test
    public void 카카오_로그인_테스트() throws JsonProcessingException {
        // code값
        final String clientId = "8686bc57b1236910b70b924fcfd89996";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", "http://localhost:8080/login/oauth2/code/kakao");
        params.add("code", "kMqInrPtodeN2hlQDQKID3LvADC_HDOpHq3dO40oNewb6cUry8ajx9G8VGyAPynzhV66aQorDNMAAAGBYP_DQQ");

        HttpEntity<MultiValueMap<String, String>> kakaoRequest = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = this.restTemplate.exchange("https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoRequest,
                String.class
        );
        System.out.println(response.getBody());
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> kakaoTokenParams = objectMapper.readValue(response.getBody(), Map.class);
        System.out.println(kakaoTokenParams.get("access_token"));
        String accessToken = kakaoTokenParams.get("access_token");

        if(!StringUtils.isNullOrEmpty(accessToken)){
            HttpHeaders headers2 = new HttpHeaders();
            headers2.add("Authorization", "Bearer "+accessToken);
            headers2.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

            HttpEntity<HttpHeaders> kakaoRequest2 = new HttpEntity<>(headers2);

            ResponseEntity<String> profileResponse = this.restTemplate.exchange("https://kapi.kakao.com/v2/user/me",
                    HttpMethod.GET,
                    kakaoRequest2,
                    String.class);

            System.out.println(profileResponse.getBody());
        }
    }
}