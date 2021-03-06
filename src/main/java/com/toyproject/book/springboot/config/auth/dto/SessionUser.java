package com.toyproject.book.springboot.config.auth.dto;

import com.toyproject.book.springboot.domian.user.User;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

/**
 * 인증된 사용자 정보를 담기 위한 클래스 (직렬화)
 *  = 직렬화 기능을 가진 세션 Dto
 */
@Getter
public class SessionUser implements Serializable {
    private String name;
    private String email;
    private String picture;

    @Builder
    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
