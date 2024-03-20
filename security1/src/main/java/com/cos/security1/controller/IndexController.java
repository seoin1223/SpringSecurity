package com.cos.security1.controller;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // View return
public class IndexController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/test/login")
    public @ResponseBody String loginTest(Authentication authentication, @AuthenticationPrincipal PrincipalDetails userDetails){ // DI(의존성 주입)
        System.out.println("/test/login =================");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authentication : "+ principalDetails.getUser());

        System.out.println("authentication : "+ userDetails.getUser());
        return "세션정보 확인";
    }

    @GetMapping("/test/oauth/login")
    public @ResponseBody String TestOAuthLogin(Authentication authentication, @AuthenticationPrincipal OAuth2User oAuth){ // DI(의존성 주입)
        System.out.println("/test/login =================");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("authentication : "+ oAuth2User.getAttributes());
        System.out.println("oauth2User : "+ oAuth.getAttributes());

        return "OAuth 세션정보 확인";
    }


    @GetMapping({"","/"})
    public String index(){
        // 기본폴더 src/main/resources/
        // 뷰 리졸버 설정 : templates(prefix), .mustacheZ(suffix) 생략 가능
        return "index";
    }

    @GetMapping("/user")
    public @ResponseBody String user(){
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin(){
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager(){
        return "manager";
    }

    // 스프링 시큐리티가 해당 주소를 가져감 - SecurityConfig 파일 생성 후 작동 안 뺏어감

    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm(){
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user){
        System.out.println(user);
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);

        userRepository.save(user);
        // 회원가입 -> 비밀번호 암호화가 되지 않음 => 시큐리티로 로그인을 할 수 없음
        // 이유는 패스워드가 암호화 안되었기 때문에 따라서 BcryptPasswordEncoder 사용
        return "redirect:/loginForm";
    }


    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody String info(){
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") // 함수가 시작되기 직전에 ROLE 검사
    // @PostAuthorize() 함수가 되고나서 활성화
    @GetMapping("/data")
    public @ResponseBody String data(){
        return "데이터 정보";
    }




}
