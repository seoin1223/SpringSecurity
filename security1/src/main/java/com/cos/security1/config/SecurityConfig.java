package com.cos.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration // IoC 빈(bean)을 등록
@EnableWebSecurity //스프링 시큐리티 필터가 스프링 필터체인에 등록
public class SecurityConfig {


    @Bean // 해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다.
    public BCryptPasswordEncoder encodePwd(){
        return new BCryptPasswordEncoder();
    }

    /*
    기존: WebSecurityConfigurerAdapter를 상속하고 configure매소드를 오버라이딩하여 설정하는 방법
    => 현재: SecurityFilterChain을 리턴하는 메소드를 빈에 등록하는 방식(컴포넌트 방식으로 컨테이너가 관리)
    //https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter


        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin").access("\"hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll();
    }
     */
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        /*
        http.csrf((csrf) -> csrf.disable());
        */
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/user/**").authenticated() // "/user/**"로 시작하는 요청은 인증이 필요합니다
                                .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER") // "/manager/**"로 시작하는 요청은 "ADMIN" 또는 "MANAGER" 역할이 필요합니다
                                .requestMatchers("/admin/**").hasAnyRole("ADMIN") // "/admin/**"로 시작하는 요청은 "ADMIN" 역할이 필요합니다
                                .anyRequest().permitAll() // 다른 모든 요청은 인증 없이 허용됩니다
                )
                .formLogin(login ->
                        login
                                .loginPage("/loginForm") // 사용자 지정 로그인 페이지 URL 설정
                                //.defaultSuccessUrl("/view/dashboard", true) // 성공 시 대시보드로 이동
                                //.permitAll() // 로그인 페이지는 모든 사용자에게 허용됩니다
                );
        return http.build(); // 구성된 SecurityFilterChain 반환
    }


}


/*
 * error 403 : 접근 권환 없음
 *
 *
 * */
