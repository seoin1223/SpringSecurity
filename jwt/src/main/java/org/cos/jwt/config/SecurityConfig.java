package org.cos.jwt.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CorsConfig config;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.addFilter(config.corsFilter()) // CrossORigin(인증x) , 시큐리티 필터에 등록 (인증 0)
            .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // session을 사용하지 않음
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request ->
                        request.requestMatchers("/api/v1/user/**").hasAnyRole("ADMIN", "MANAGER", "USER")
                                .requestMatchers("/api/v1/manager/**").hasAnyRole("ADMIN", "MANAGER")
                                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                                .anyRequest().permitAll()
                );

    return http.build();
    }
}
