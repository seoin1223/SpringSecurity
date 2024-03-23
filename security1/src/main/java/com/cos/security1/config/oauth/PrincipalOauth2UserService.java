package com.cos.security1.config.oauth;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.config.oauth.provider.FacebookUserInfo;
import com.cos.security1.config.oauth.provider.GoogleUserInfo;
import com.cos.security1.config.oauth.provider.NaverUserInfo;
import com.cos.security1.config.oauth.provider.OAuth2UserInfo;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

//    @Autowired // 해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다.
//    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomBCryptPasswordEncoder customBCryptPasswordEncoder;



    // 구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
    // 함수 종료 시 @AuthenticaionPrincipal 어노테이션이 만들어진다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
      System.out.println("getClientRegistration: "+userRequest.getClientRegistration());  // registrationId로 어떤 OAuth로 가입했는지 알 수 있음
        System.out.println("getAccessToken: "+userRequest.getAccessToken().getTokenValue());


        OAuth2User oAuth2User = super.loadUser(userRequest);
        // 구글 로그인 버튼 클릭 -> 구글 로그인창 -> 로그인 완료 -> code를 리턴(OAuth-CLient Lib) -> AccessToken 요청 == UserRequest 정보
        // userRequest 정보 -> loadUser method(회원 프로필을 받아야함) -> 구글로부터 회원 프로필을 받아준다
        System.out.println("getAttributes : " +oAuth2User.getAttributes());

        OAuth2UserInfo oAuth2UserInfo = null;

        if(userRequest.getClientRegistration().getRegistrationId().equals("google")){
            System.out.println("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }else if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")){
            System.out.println("페이스북 로그인 요청");
            oAuth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());
        }else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")){
            System.out.println("네이버 로그인 요청");
            oAuth2UserInfo = new NaverUserInfo((Map)oAuth2User.getAttributes().get("response"));
        }
        else{
            System.out.println("우리는 구글과 페이스북, 네이버만 지원합니다.");
        }

        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();

        String username = provider+"_"+providerId;
        String password = customBCryptPasswordEncoder.encode(username);
        String providerEmail = oAuth2UserInfo.getEmail(); // google
        String role = "ROLE_USER";

        User userEntity = userRepository.findByUsername(username);

        if(userEntity == null){
            System.out.println("최초 가입");
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .provider(provider)
                    .providerId(providerId)
                    .email(providerEmail)
                    .role(role)
                    .build();
            userRepository.save(userEntity);
        }else{
            System.out.println("로그인을 이미 한적이 있습니다. 당신은 자동회원가입이 되어있습니다.");
        }


//        return super.loadUser(userRequest);
        return new PrincipalDetails(userEntity,oAuth2User.getAttributes());
    }
}

/*
* username = google_sub
* password = "암호화(username)"
* email = email
* role = "ROLE_USER"
* provider = "google"
* providerId = sub
* */


/*
 // 회원가입 강제 진행

        String provider = userRequest.getClientRegistration().getClientName(); // google
        String providerId = oAuth2User.getAttribute("sub"); // google

        String username = provider+"_"+providerId;

        String password = customBCryptPasswordEncoder.encode(username);
        String providerEmail = oAuth2User.getAttribute("email"); // google
        String role = "ROLE_USER";


        User userEntity = userRepository.findByUsername(username);




        if(userEntity == null){
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .provider(provider)
                    .providerId(providerId)
                    .email(providerEmail)
                    .role(role)
                    .build();
            userRepository.save(userEntity);
        }

* */