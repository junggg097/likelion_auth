package com.example.auth.oauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class OAuth2UserServiceImpl
        // 기본적인 OAuth2 인증 과정을 진행 해주는 클래스
        extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        // 어떤 서비스 제공자를 사용 했는지
        String registrationId = userRequest
                .getClientRegistration()
                .getRegistrationId();
                
        // TODO 서비스 제공자에 따라 데이터 처리르 달리 하고 싶을 때

        // OAuth2 제공자 로부터 받은 데이터를 원하는 방식 다시 정리 하기 위한 Map
        Map<String, Object> attributes = new HashMap<>();
        String nameAttribute ="";

        // Naver 아이디로 로그인
        if(registrationId.equals("naver")){
            // Naver 에서 받아온 정보다
            attributes.put("provider", "naver");

            Map<String, Object> responseMap
                    // 네이버가 반환한 JSON에서 response를 회수
                    = oAuth2User.getAttribute("response");
            attributes.put("id", responseMap.get("id"));
            attributes.put("email", responseMap.get("email"));
            attributes.put("nickname", responseMap.get("nickname"));
            attributes.put("name", responseMap.get("name"));
            attributes.put("profileImg", responseMap.get("profileImg"));
        }
        log.info(attributes.toString());
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                nameAttribute
        );
    }
}
