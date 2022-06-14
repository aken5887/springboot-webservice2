package com.toyproject.book.springboot.config.auth.dto;


import com.toyproject.book.springboot.domian.user.Role;
import com.toyproject.book.springboot.domian.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

/**
 * OAuth2UserService를 통해서 가져온 OAuth2User의 attribute를 담을 클래스
 */
@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes){
        if("naver".equals(registrationId)){
            return ofNaver("id", attributes);
        }

        if("kakao".equals(registrationId)){
            return ofKakao("id", attributes);
        }
       return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        // kakao는 kakao_account에 유저정보가 있음
        /**
         * {
         * "id": 2285778181,
         * "connected_at": "2022-06-14T05:37:10Z",
         * "properties":{
         * "nickname": "용훈",
         * "profile_image": "http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_640x640.jpg",
         * "thumbnail_image": "http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_110x110.jpg"
         * },
         * "kakao_account":{
         * "profile_nickname_needs_agreement": false,
         * "profile_image_needs_agreement": false,
         * "profile":{
         * "nickname": "용훈",
         * "thumbnail_image_url": "http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_110x110.jpg",
         * "profile_image_url": "http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_640x640.jpg",
         * "is_default_image": true
         * },
         * "has_email": true,
         * "email_needs_agreement": false,
         * "is_email_valid": true,
         * "is_email_verified": true,
         * "email": "aken@kakao.com"
         * }
         *}
         */
        Map<String, Object> kakaoAccouint = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccouint.get("profile");

        return OAuthAttributes.builder()
                .name((String) kakaoProfile.get("nickname"))
                .email((String) kakaoAccouint.get("email"))
                .picture((String) kakaoProfile.get("profile_image_url"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();

    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String)response.get("profile_image"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)    
                .build();
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes){
        return OAuthAttributes.builder()
                .name(getAttribute(attributes, "name"))
                .email(getAttribute(attributes, "email"))
                .picture(getAttribute(attributes, "picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
    private static String getAttribute(Map<String, Object> attributes, String key){
        return (String) attributes.get(key);
    }

    public User toEntity(){
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.GUEST)
                .build();
    }
}
