package com.cos.security1.config.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    // 구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userRequest: "+userRequest.getClientRegistration());  // registrationId로 어떤 OAuth로 가입했는지 알 수 있음
        System.out.println("userRequest: "+userRequest.getAccessToken().getTokenValue());

        // 구글 로그인 버튼 클릭 -> 구글 로그인창 -> 로그인 완료 -> code를 리턴(OAuth-CLient Lib) -> AccessToken 요청 == UserRequest 정보

        // userRequest 정보 -> loadUser method(회원 프로필을 받아야함) -> 구글로부터 회원 프로필을 받아준다
        System.out.println("userRequest: "+super.loadUser(userRequest).getAttributes());

         OAuth2User oAuth2User = super.loadUser(userRequest);

        // 회원가입 강제 진행
        return super.loadUser(userRequest);
    }
}

/*
* username = google_sub
* password = "암호화(겟인데어)"
* email = email
* role = "ROLE_USER"
* provider = "google"
* providerId = sub
*
* */
