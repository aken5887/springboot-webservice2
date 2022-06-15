package com.toyproject.book.springboot.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;

public class ProfileControllerUnitTest {

    /**
     *  ProfileController의 Envionemnt가 생성자 DI 방식이기 떄문에
     *  SpringBootTest없이 가능했다!! ( @Autowired 방식을 쓰지 않기 떄문 )
     */
    @Test
    public void real_profile이_조회된다() {
         // given
        String expectedProfile = "real";
        MockEnvironment env = new MockEnvironment();
        env.addActiveProfile(expectedProfile);
        env.addActiveProfile("oauth");
        env.addActiveProfile("real-db");

        ProfileController pc = new ProfileController(env);

        // when
        String profile = pc.profile();

        // then
        assertThat(profile).isEqualTo(expectedProfile);
    }

    @Test
    @DisplayName("real_profile이_없으면_첫번째가_조회된다")
    public void test2(){
        // givn
        String expectedProfile = "oauth";
        MockEnvironment env = new MockEnvironment();
        env.addActiveProfile("oauth");
        env.addActiveProfile("real-db");

        // when
        ProfileController pc = new ProfileController(env);

        // then
        assertThat(pc.profile()).isEqualTo(expectedProfile);
    }

    @Test
    @DisplayName("active_profile이_없으면_default가_조회된다.")
    void test3(){
        //given
        String expectedProfile = "default";
        MockEnvironment env = new MockEnvironment();

        //when
        ProfileController pc = new ProfileController(env);

        //then
        assertThat(pc.profile()).isEqualTo(expectedProfile);
    }
}